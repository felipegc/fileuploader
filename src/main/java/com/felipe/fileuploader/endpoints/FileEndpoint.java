package com.felipe.fileuploader.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.services.FileInfoService;
import com.felipe.fileuploader.services.FileInfoServiceImpl;
import com.felipe.fileuploader.services.FileService;
import com.felipe.fileuploader.services.FileServiceImpl;
import com.felipe.fileuploader.tos.ConverterTo;
import com.felipe.fileuploader.tos.FileInfoConverter;
import com.felipe.fileuploader.tos.FileInfoTo;
import com.felipe.fileuploader.tos.ResponseErrorTo;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.DirUtil;
import com.felipe.fileuploader.util.stream.StreamingOutputImpl;

@Path("/files")
@Api(value = "files")
public class FileEndpoint {

	FileService fileService = new FileServiceImpl();
	FileInfoService infoService = new FileInfoServiceImpl();
	ConverterTo<FileInfo, FileInfoTo> converterTo = new FileInfoConverter();

	@POST
	@Path("/upload")
	@ApiOperation(value = "Upload a chunk from a file.", response = FileInfo.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "The mandatory field is not present: nameOfField., This chunk has already been uploaded."),
			@ApiResponse(code = 500, message = "The entity z could not be saved., The chunk y of the file x could not be saved., The directory x to storage was not set properly. Contact support., We have got an internal error") })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response uploadFile(
			@ApiParam(name = "chunkNumber", value = "number of the chunk which belongs to the file being uploaded", required = true) @FormDataParam("chunkNumber") Integer chunkNumber,
			@ApiParam(name = "chunksExpected", value = "amount of chunks expected to be uploaded for the entire file", required = true) @FormDataParam("chunksExpected") Integer chunksExpected,
			@ApiParam(name = "owner", value = "owner of this file which means the person that is uploading the file", required = true) @FormDataParam("owner") String owner,
			@ApiParam(name = "name", value = "file name being uploaded", required = true) @FormDataParam("name") String name,
			@ApiParam(value = "blob that represent the chunk", required = true) @FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		FileInfo uploadFile = null;

		try {
			uploadFile = fileService.uploadFile(chunkNumber, chunksExpected,
					owner, name, uploadedInputStream, fileDetail);
		} catch (BadRequestException ex) {
			return Response
					.status(ex.getResponse().getStatus())
					.entity(new ResponseErrorTo(ex.getResponse().getStatus(),
							ex.getMessage())).build();
		} catch (InternalServerErrorException ex) {
			return Response
					.status(ex.getResponse().getStatus())
					.entity(new ResponseErrorTo(ex.getResponse().getStatus(),
							ex.getMessage())).build();
		} catch (Exception ex) {
			// The external client does not need to know this.
			return Response
					.status(500)
					.entity(new ResponseErrorTo(500, AppConfiguration
							.get("error.internal_error_message"))).build();
		}
		return Response.status(200).entity(uploadFile).build();
	}

	@GET
	@Path("download/{owner}/{fileName}")
	@ApiOperation(value = "Download a file.", response = StreamingOutput.class, notes = "This will merge the whole chunks into a single file.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "There is no such a file in our repository., The file requested is not ready for downloading.")})
	@ApiResponse(code = 500, message = "One of the chunks could not be found., The chunks could not be merged.")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON })
	public Response downloadFile(
			@ApiParam(value = "owner who upload the file", required = true) @PathParam("owner") String owner,
			@ApiParam(value = "file name", required = true) @PathParam("fileName") String fileName) {

		StreamingOutput file;

		try {
			File downloadFile = fileService.downloadFile(owner, fileName);
			file = new StreamingOutputImpl(downloadFile.getPath());
		} catch (BadRequestException ex) {
			return Response
					.status(ex.getResponse().getStatus())
					.entity(new ResponseErrorTo(ex.getResponse().getStatus(),
							ex.getMessage())).build();
		} catch (InternalServerErrorException ex) {
			return Response
					.status(ex.getResponse().getStatus())
					.entity(new ResponseErrorTo(ex.getResponse().getStatus(),
							ex.getMessage())).build();
		} catch (Exception ex) {
			// The external client does not need to know
			return Response
					.status(500)
					.entity(new ResponseErrorTo(500, AppConfiguration
							.get("error.internal_error_message"))).build();
		}
		return Response
				.ok(file)
				.header("Content-Disposition",
						"attachment; filename=" + fileName).build();
	}

	@GET
	@Path("/list")
	@ApiOperation(value = "Lists all information related to the uploaded files such as time spent, number of chunks and the owner of it.", response = FileInfo.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "We have got an internal error") })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListChunks() {

		Map<String, List<FileInfo>> listAllChunkInfo = infoService
				.listAllChunkInfo();
		List<FileInfoTo> infosTo = new ArrayList<>();

		for (String id : listAllChunkInfo.keySet()) {
			List<FileInfo> chunks = listAllChunkInfo.get(id);
			infosTo.add(converterTo.converterFromEntityTo(chunks));
		}

		return Response.ok().entity(infosTo).build();
	}

	@GET
	@Path("/erase")
	@ApiOperation(value = "Erase the whole database for this app.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Database successfully erased."),
			@ApiResponse(code = 500, message = "We have got an internal error") })
	@Produces(MediaType.APPLICATION_JSON)
	public Response eraseDataBase() {
		try {
			FileUtils.cleanDirectory(new File(DirUtil.getDirDataBase()));
		} catch (IOException e) {
			Response.status(500)
					.entity(new ResponseErrorTo(500, AppConfiguration
							.get("error.internal_error_message"))).build();
		}
		return Response.ok(200).entity("Database successfully erased.").build();
	}
}