package com.felipe.fileuploader.endpoints;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.felipe.fileuploader.services.FileServiceImpl;

@Path("/files")
public class FileEndpoint {

	FileServiceImpl fileService = new FileServiceImpl();

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response uploadFile(@FormDataParam("chunkNumber") Integer chunkNumber,
			@FormDataParam("owner") String owner,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		try {
			fileService.uploadFile(chunkNumber, owner, uploadedInputStream, fileDetail);
		} catch (ServerErrorException ex) {
			return Response.status(ex.getResponse().getStatus())
					.entity(ex.getMessage()).build();
		}

		String output = "File successfully uploaded";

		return Response.status(200).entity(output).build();
	}
}