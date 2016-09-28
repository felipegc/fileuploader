package com.felipe.fileuploader.services;

import com.felipe.fileuploader.entities.FileInfo;

public interface FileInfoService extends GenericService<FileInfo, String> {
	
	public FileInfo saveFileInformation(String name, String owner,
			String status, Integer chunkNumber, Long initTimestamp,
			Long finalTimestamp);
}
