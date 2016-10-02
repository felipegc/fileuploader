package com.felipe.fileuploader.services;

import java.util.List;
import java.util.Map;

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
			Integer chunkNumber, Integer chunksExpected, StatusUpload status, Long initTimestamp,
			Long finalTimestamp) {
		FileInfo info = new FileInfo();
		info.setId(owner + name);
		info.setName(name);
		info.setOwner(owner);
		info.setStatus(status);
		info.setChunkNumber(chunkNumber);
		info.setAmountOfChunks(chunksExpected);
		info.setInitTimestamp(initTimestamp);
		info.setFinalTimestamp(finalTimestamp);

		dao.save(info);

		return info;
	}

	@Override
	public List<FileInfo> retrieveAllInfoChunksByOwnerName(String owner,
			String name) {
		return dao.findChunksInfoByOwnerName(owner, name);
	}
	
	@Override
	public Map<String,List<FileInfo>> listAllChunkInfo(){
		return dao.listAllChunkInfoOrganizedById();
	}
	
	@Override
	public StatusUpload defineStatus(List<FileInfo> chunks) {
		if (chunks.size() == 0) {
			return StatusUpload.PROGRESS;
		} else if (chunks.size() == chunks.get(0).getAmountOfChunks()) {
			return StatusUpload.FINISHED;
		} else {
			for (FileInfo fileInfo : chunks) {
				if (StatusUpload.FAILED.equals(fileInfo.getStatus())) {
					return StatusUpload.FAILED;
				}
			}
			return StatusUpload.PROGRESS;
		}
	}
}
