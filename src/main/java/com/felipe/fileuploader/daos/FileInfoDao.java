package com.felipe.fileuploader.daos;

import java.util.List;

import com.felipe.fileuploader.entities.FileInfo;

public interface FileInfoDao extends GenericDao<FileInfo, String> {
	public List<FileInfo> findChunksInfoByOwnerName(String owner, String name);
}
