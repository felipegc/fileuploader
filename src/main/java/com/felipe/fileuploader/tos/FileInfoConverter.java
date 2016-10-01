package com.felipe.fileuploader.tos;

import java.util.List;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;
import com.felipe.fileuploader.util.TimeUtil;

public class FileInfoConverter implements ConverterTo<FileInfo, FileInfoTo> {

	@Override
	public FileInfoTo converterFromEntityTo(List<FileInfo> chunks) {
		FileInfoTo infoTo = new FileInfoTo();
		infoTo.setId(chunks.get(0).getId());
		infoTo.setNumberOfChunks(chunks.size());
		infoTo.setName(chunks.get(0).getName());
		infoTo.setOwner(chunks.get(0).getOwner());
		infoTo.setStatus(defineStatus(chunks));
		infoTo.setSecondsSpent(calculateSecondsSpent(chunks));

		return infoTo;
	}

	private StatusUpload defineStatus(List<FileInfo> chunks) {
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

	private Long calculateSecondsSpent(List<FileInfo> chunks) {
		Long secondsSpent = 0L;
		for (FileInfo chunk : chunks) {
			Long timeSpent = chunk.getFinalTimestamp() - chunk.getInitTimestamp();
			secondsSpent += timeSpent;
		}

		return secondsSpent / 1000;
	}
}
