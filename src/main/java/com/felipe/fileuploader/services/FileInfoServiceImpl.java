package com.felipe.fileuploader.services;

import com.felipe.fileuploader.daos.FileInfoDao;
import com.felipe.fileuploader.daos.FileInfoDaoImpl;
import com.felipe.fileuploader.entities.FileInfo;

public class FileInfoServiceImpl extends
		GenericServiceImpl<FileInfo, String, FileInfoDao> implements
		FileInfoService {

	public FileInfoServiceImpl() {
		super(new FileInfoDaoImpl());
	}
	
	public FileInfo saveFileInformation(String name, String owner,
			String status, Integer chunkNumber, Long initTimestamp,
			Long finalTimestamp) {
		FileInfo info = new FileInfo();
		info.setId(name+owner);
		info.setName(name);
		info.setOwner(owner);
		info.setStatus(status);
		info.setChunkNumber(chunkNumber);
		info.setInitTimestamp(initTimestamp);
		info.setFinalTimestamp(finalTimestamp);
		
		dao.save(info);
		
		return info;
	}
}
