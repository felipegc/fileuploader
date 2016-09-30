package com.felipe.fileuploader.endpoints;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.services.FileInfoService;
import com.felipe.fileuploader.services.FileInfoServiceImpl;
import com.felipe.fileuploader.services.FileServiceImpl;
import com.felipe.fileuploader.tos.ConverterTo;
import com.felipe.fileuploader.tos.FileInfoConverter;
import com.felipe.fileuploader.tos.FileInfoTo;

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

		try {
			// if(chunkNumber == 1){
			// throw new InternalServerErrorException();
			// }
			fileService.uploadFile(chunkNumber, chunksExpected, owner, name,
					uploadedInputStream, fileDetail);
		} catch (ServerErrorException ex) {
			return Response.status(ex.getResponse().getStatus())
					.entity(ex.getMessage()).build();
		}

		String output = "File successfully uploaded";

		return Response.status(200).entity(output).build();
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
}