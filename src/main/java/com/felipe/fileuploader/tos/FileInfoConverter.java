package com.felipe.fileuploader.tos;

import java.util.LinkedList;
import java.util.List;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.util.TimeUtil;

public class FileInfoConverter implements ConverterTo<FileInfo, FileInfoTo> {

	@Override
	public FileInfoTo converterFromEntityTo(List<FileInfo> chunks) {
		FileInfoTo infoTo = new FileInfoTo();
		infoTo.setId(chunks.get(0).getId());
		infoTo.setNumberOfChunks(chunks.size());
		infoTo.setName(chunks.get(0).getName());
		infoTo.setOwner(chunks.get(0).getOwner());
		
		LinkedList<FileInfo> convertedList = (LinkedList<FileInfo>) chunks;

		infoTo.setStatus(convertedList.getLast().getStatus());
		infoTo.setMinutesSpent(TimeUtil.extractMinutesFromTimestamps(
				convertedList.getLast().getInitTimestamp(), convertedList
						.getFirst().getInitTimestamp()));

		return infoTo;
	}

}
