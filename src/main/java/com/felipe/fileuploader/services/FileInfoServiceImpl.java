package com.felipe.fileuploader.services;

import com.felipe.fileuploader.daos.FileInfoDao;
import com.felipe.fileuploader.daos.FileInfoDaoImpl;
import com.felipe.fileuploader.entities.FileInfo;

public class FileInfoServiceImpl extends
GenericServiceImpl<FileInfo, String, FileInfoDao> implements
FileInfoService{

	public FileInfoServiceImpl() {
		super(new FileInfoDaoImpl());
	}
}
