package com.felipe.fileuploader.services;

import java.io.File;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.felipe.fileuploader.entities.FileInfo;

public interface FileService {
	public FileInfo uploadFile(Integer chunkNumber, Integer chunksExpected,
			String owner, String fileName, InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail);
	
	public File downloadFile(String owner, String fileName);
}
