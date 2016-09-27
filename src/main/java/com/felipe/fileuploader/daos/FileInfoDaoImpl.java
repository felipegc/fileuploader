package com.felipe.fileuploader.daos;

import com.felipe.fileuploader.entities.FileInfo;

public class FileInfoDaoImpl extends GenericDaoImpl<FileInfo, String> implements FileInfoDao {
	
	public static final String DATA_BASE_FILE_NAME = "fileInfo.db";
	
	public FileInfoDaoImpl() {
		super(DATA_BASE_FILE_NAME);
	}
}
