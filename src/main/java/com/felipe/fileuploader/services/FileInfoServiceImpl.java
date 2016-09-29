package com.felipe.fileuploader.services;

import java.util.List;

import com.felipe.fileuploader.daos.FileInfoDao;
import com.felipe.fileuploader.daos.FileInfoDaoImpl;
import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;

public class FileInfoServiceImpl extends
		GenericServiceImpl<FileInfo, String, FileInfoDao> implements
		FileInfoService {

	public FileInfoServiceImpl() {
		super(new FileInfoDaoImpl());
	}

	public FileInfo saveFileInformation(String owner, String name,
			Integer chunkNumber, Integer chunksExpected, Long initTimestamp,
			Long finalTimestamp) {
		FileInfo info = new FileInfo();
		info.setId(owner + name);
		info.setName(name);
		info.setOwner(owner);
		info.setStatus(chunkNumber == chunksExpected ? StatusUpload.FINISHED
				.name() : StatusUpload.PROGRESS.name());
		info.setChunkNumber(chunkNumber);
		info.setInitTimestamp(initTimestamp);
		info.setFinalTimestamp(finalTimestamp);

		dao.save(info);

		return info;
	}

	@Override
	public List<FileInfo> retrieveAllInfoChunksByOwnerName(String owner, String name) {
		return dao.findChunksInfoByOwnerName(owner, name);
	}
}
