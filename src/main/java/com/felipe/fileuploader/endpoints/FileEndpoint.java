package com.felipe.fileuploader.endpoints;

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
import com.felipe.fileuploader.services.FileServiceImpl;
import com.felipe.fileuploader.tos.ConverterTo;
import com.felipe.fileuploader.tos.FileInfoConverter;
import com.felipe.fileuploader.tos.FileInfoTo;
import com.felipe.fileuploader.tos.ResponseErrorTo;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.DirUtil;
import com.felipe.fileuploader.util.StreamingOutputImpl;

@Path("/files")
public class FileEndpoint {

	FileServiceImpl fileService = new FileServiceImpl();
	FileInfoService infoService = new FileInfoServiceImpl();
	ConverterTo<FileInfo, FileInfoTo> converterTo = new FileInfoConverter();

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response uploadFile(
			@FormDataParam("chunkNumber") Integer chunkNumber,
			@FormDataParam("chunksExpected") Integer chunksExpected,
			@FormDataParam("owner") String owner,
			@FormDataParam("name") String name,
			@FormDataParam("file") InputStream uploadedInputStream,
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
			return Response.status(ex.getResponse().getStatus())
					.entity(ex.getMessage()).build();
		} catch (Exception ex) {
			// Bad code falls here. The external client does not need to know
			// this.
			return Response
					.status(500)
					.entity(new String(AppConfiguration
							.get("error.internal_error_message"))).build();
		}
		return Response.status(200).entity(uploadFile).build();
	}

	@GET
	@Path("/list")
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
	@Path("download/{owner}/{fileName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("owner") String owner, @PathParam("fileName") String fileName){
		
		StreamingOutput file;
		
		try {
			File downloadFile = fileService.downloadFile(owner, fileName);
			file = new StreamingOutputImpl(downloadFile.getPath());
		} catch (BadRequestException ex) { 
			//TODO felipeg check if we can send json as response, it might need to put in the list of produces
			return Response
					.status(ex.getResponse().getStatus())
					.entity(new ResponseErrorTo(ex.getResponse().getStatus(),
							ex.getMessage())).build();
		} catch (InternalServerErrorException ex) {
			return Response.status(ex.getResponse().getStatus())
					.entity(ex.getMessage()).build();
		} catch (Exception ex) {
			// Bad code falls here. The external client does not need to know
			// this.
			return Response
					.status(500)
					.entity(AppConfiguration.get("error.internal_error_message")).build();
		}
		return Response
                .ok(file)
                .header("Content-Disposition","attachment; filename="+fileName)
                .build();
	} 
	
	@GET
	@Path("/erase")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eraseDataBase() {
		try {
			FileUtils.cleanDirectory(new File(DirUtil.getDirDataBase()));
		} catch (IOException e) {
			Response.status(500)
					.entity(AppConfiguration.get("error.internal_error_message"))
					.build();
		}
		return Response.ok().build();
	}
}