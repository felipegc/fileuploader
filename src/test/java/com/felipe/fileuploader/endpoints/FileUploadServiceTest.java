package com.felipe.fileuploader.endpoints;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Ignore;
import org.junit.Test;

import com.felipe.fileuploader.util.DirUtil;

public class FileUploadServiceTest {

	@Test
    @Ignore
	public void test() throws IOException {

		final Client client = ClientBuilder.newBuilder()
				.register(MultiPartFeature.class).build();
		WebTarget t = client.target(
				"http://localhost:8080/fileuploader/rest/files/")
				.path("upload");

		FileDataBodyPart filePart = new FileDataBodyPart("file", new File(
				"src/main/resources/bigtext.txt"));
		filePart.setContentDisposition(FormDataContentDisposition.name("file")
				.fileName("savedBigText.txt").build());

		String empPartJson = "{\n" + "    \"id\": 1234,\n"
				+ "    \"name\": \"Peeskillet\"\n" + "}\n" + "";

		MultiPart multipartEntity = new FormDataMultiPart()
		// .field("emp", empPartJson, MediaType.APPLICATION_JSON_TYPE)
				.bodyPart(filePart);

		Response response = t.request().post(
				Entity.entity(multipartEntity, MediaType.MULTIPART_FORM_DATA));
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));

		response.close();
		multipartEntity.close();
	}

	@Test
	@Ignore
	public void whenSendingMultipleChunksMustGenerateOneFileInfoForEachChunk()
			throws IOException {

		final Client client = ClientBuilder.newBuilder()
				.register(MultiPartFeature.class).build();
		
		WebTarget t = client.target(
				"http://localhost:8080/fileuploader/rest/files/")
				.path("upload");
		
		File file = new File("src/main/resources/bigtext.txt");
		
		Integer chunkId = 1;
		Integer chunkSize = 1024*1024;
		byte[] buffer = new byte[chunkSize];
		
		BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(file));
		
		try{
			String chunkName = file.getName();
			
			Integer tmp = 0;
			
			while((tmp = bis.read(buffer)) > 0){
				File newChunk = new File(DirUtil.getDirDataBase(), chunkName + "." +chunkId);	
				
				FileOutputStream out = new FileOutputStream(newChunk);
				out.write(buffer,0, tmp);
				
				FileDataBodyPart filePart = new FileDataBodyPart("file", newChunk);
				filePart.setContentDisposition(FormDataContentDisposition.name("file")
						.fileName("savedBigText.txt").build());
				MultiPart multipartEntity = new FormDataMultiPart()
				.field("chunkId", String.valueOf(chunkId)).bodyPart(filePart);

				Response response = t.request().post(
				Entity.entity(multipartEntity, MediaType.MULTIPART_FORM_DATA));
				
				chunkId++;
				
				System.out.println(response.getStatus());
				System.out.println(response.readEntity(String.class));
			}
		}finally{
			bis.close();
		}
	}

	public class JerseyStreamingOutput implements StreamingOutput {
		@Override
		public void write(OutputStream os) throws IOException,
				WebApplicationException {
			Writer writer = new BufferedWriter(new OutputStreamWriter(os));
			writer.write("test");
			writer.flush();

		}
	}
}
