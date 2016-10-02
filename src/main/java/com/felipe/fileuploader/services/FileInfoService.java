package com.felipe.fileuploader.services;

import java.util.List;
import java.util.Map;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;

public interface FileInfoService extends GenericService<FileInfo, String> {
	
	public FileInfo saveFileInformation(String owner, String name,
			Integer chunkNumber, Integer chunksExpected, StatusUpload status, Long initTimestamp,
			Long finalTimestamp);

	public List<FileInfo> retrieveAllInfoChunksByOwnerName(String owner, String name);
	
	public Map<String,List<FileInfo>> listAllChunkInfo();
	
	public StatusUpload defineStatus(List<FileInfo> chunks);
}
