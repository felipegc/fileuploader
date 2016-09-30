package com.felipe.fileuploader.daos;

import java.util.List;
import java.util.Map;

import com.felipe.fileuploader.entities.FileInfo;

public interface FileInfoDao extends GenericDao<FileInfo, String> {
	
	public List<FileInfo> findChunksInfoByOwnerName(String owner, String name);
	
	public Map<String,List<FileInfo>> listAllChunkInfoOrganizedById();
}
