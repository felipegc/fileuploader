package com.felipe.fileuploader.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.InternalServerErrorException;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;
import com.felipe.fileuploader.util.AppConfiguration;

public class FileServiceImpl {

	FileInfoService fileInfoService = new FileInfoServiceImpl();

	public void uploadFile(Integer chunkNumber, String owner, InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail) {
		
		try {
			
			Long initTimestamp = new Date().getTime();
			
			String fileLocation = AppConfiguration.get("files.storage")
					+ fileDetail.getFileName() + chunkNumber;

			FileOutputStream out = new FileOutputStream(new File(fileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(fileLocation));
			read = uploadedInputStream.read(bytes);
			while (read  != -1) {
				out.write(bytes, 0, read);
				read = uploadedInputStream.read(bytes);
			}
			
			out.flush();
			out.close();
			
			Long finalTimestamp = new Date().getTime();
			
			fileInfoService.saveFileInformation(
					fileDetail.getFileName(), owner, StatusUpload.PROGRESS.name(), chunkNumber, initTimestamp, finalTimestamp);
		} catch (FileNotFoundException ex) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.directory_not_found",
					AppConfiguration.get("files.storage")));
		} catch (IOException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.chunk_not_saved", chunkNumber, fileDetail.getFileName()));
		}
	}
}
