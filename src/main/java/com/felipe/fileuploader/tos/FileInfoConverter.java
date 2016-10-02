package com.felipe.fileuploader.tos;

import java.util.List;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.services.FileInfoService;
import com.felipe.fileuploader.services.FileInfoServiceImpl;

public class FileInfoConverter implements ConverterTo<FileInfo, FileInfoTo> {
	
	FileInfoService infoService = new FileInfoServiceImpl();
	
	@Override
	public FileInfoTo converterFromEntityTo(List<FileInfo> chunks) {
		FileInfoTo infoTo = new FileInfoTo();
		infoTo.setId(chunks.get(0).getId());
		infoTo.setNumberOfChunks(chunks.size());
		infoTo.setName(chunks.get(0).getName());
		infoTo.setOwner(chunks.get(0).getOwner());
		infoTo.setStatus(infoService.defineStatus(chunks));
		infoTo.setSecondsSpent(calculateSecondsSpent(chunks));

		return infoTo;
	}

	private Long calculateSecondsSpent(List<FileInfo> chunks) {
		Long secondsSpent = 0L;
		for (FileInfo chunk : chunks) {
			Long timeSpent = chunk.getFinalTimestamp() - chunk.getInitTimestamp();
			secondsSpent += timeSpent;
		}

		return secondsSpent / 1000;
	}
}
