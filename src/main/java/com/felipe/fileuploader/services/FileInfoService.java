package com.felipe.fileuploader.services;

import java.util.List;

import com.felipe.fileuploader.entities.FileInfo;

public interface FileInfoService extends GenericService<FileInfo, String> {
	
	public FileInfo saveFileInformation(String owner, String name,
			Integer chunkNumber, Integer chunksExpected, Long initTimestamp,
			Long finalTimestamp);

	public List<FileInfo> retrieveAllInfoChunksByOwnerName(String owner, String name);
}
