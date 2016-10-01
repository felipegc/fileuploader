package com.felipe.fileuploader.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.DirUtil;

public class FileServiceImpl implements FileService{

	FileInfoService fileInfoService = new FileInfoServiceImpl();

	public synchronized FileInfo uploadFile(Integer chunkNumber,
			Integer chunksExpected, String owner, String fileName,
			InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail) {

		Long initTimestamp = new Date().getTime();
		Long finalTimestamp = null;
		FileOutputStream out = null;

		try {

			validateUpload(chunkNumber, chunksExpected, owner, fileName,
					uploadedInputStream);

			String fileLocation = DirUtil.createDirForChunksIfNotExist(owner,
					fileName) + fileName + chunkNumber;

			out = new FileOutputStream(new File(fileLocation));

			copyBuffer(uploadedInputStream, out);

			finalTimestamp = new Date().getTime();

			return fileInfoService.saveFileInformation(owner, fileName,
					chunkNumber, chunksExpected, StatusUpload.PROGRESS,
					initTimestamp, finalTimestamp);
		} catch (FileNotFoundException ex) {
			fileInfoService.saveFileInformation(owner, fileName, chunkNumber,
					chunksExpected, StatusUpload.FAILED, initTimestamp,
					new Date().getTime());
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.directory_not_found",
					AppConfiguration.get("files.storage")));

		} catch (IOException e) {
			fileInfoService.saveFileInformation(owner, fileName, chunkNumber,
					chunksExpected, StatusUpload.FAILED, initTimestamp,
					new Date().getTime());
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.chunk_not_saved", chunkNumber, fileName));
		} finally {
			DirUtil.freeOSResources(out);
		}
	}

	public File downloadFile(String owner, String fileName) {

		BufferedOutputStream mergingStream = null;
		File fileMerged = null;
		try {
			List<FileInfo> chunks = fileInfoService
					.retrieveAllInfoChunksByOwnerName(owner, fileName);
			
			validateDownload(chunks);

			Collections.sort(chunks);

			String chunksDir = DirUtil.getDirByOwnerAndFileName(owner,
					fileName.split("\\.")[0]);

			fileMerged = new File(chunksDir + DirUtil.getSlashUsed() + fileName);

			mergingStream = new BufferedOutputStream(new FileOutputStream(
					fileMerged, true));

			for (FileInfo fileInfo : chunks) {
				File file = new File(fileMerged.getAbsolutePath()
						+ fileInfo.getChunkNumber());
				mergeFile(mergingStream, file);
			}

		} catch (FileNotFoundException ex) {
			throw new InternalServerErrorException(
					AppConfiguration.get("bad.chunk_not_found"));

		} catch (IOException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.file_not_merged", fileName));
		} finally {
			IOUtils.closeQuietly(mergingStream);
		}

		return fileMerged;
	}

	private void mergeFile(OutputStream os, File source) throws IOException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(source));
			copyBuffer(is, os);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private long copyBuffer(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	private void validateUpload(Integer chunkNumber, Integer chunksExpected,
			String owner, String name, InputStream uploadedInputStream) {

		validateMandatoryFields(chunkNumber, chunksExpected, owner, name,
				uploadedInputStream);
		validateSameChunkUploaded(owner, name, chunkNumber);

	}
	
	private void validateDownload(List<FileInfo> infoChunks){
		validateFileExists(infoChunks);
		validateIsReadyToDownload(infoChunks);
	}

	private void validateMandatoryFields(Integer chunkNumber,
			Integer chunksExpected, String owner, String name,
			InputStream uploadedInputStream) {

		String field = null;

		if (chunkNumber == null) {
			field = "chunkNumber";
		}

		if (chunksExpected == null) {
			field = "chunksExpected";
		}

		if (StringUtils.isEmpty(owner)) {
			field = "owner";
		}

		if (StringUtils.isEmpty(name)) {
			field = "name";
		}

		if (uploadedInputStream == null) {
			field = "file";
		}

		if (field != null) {
			throw new BadRequestException(AppConfiguration.get(
					"bad.mandatory_field_not_present", field));
		}
	}

	private void validateSameChunkUploaded(String owner, String name,
			Integer chunkNumber) {
		List<FileInfo> retrieveAllInfoChunksByOwnerName = fileInfoService
				.retrieveAllInfoChunksByOwnerName(owner, name);
		for (FileInfo fileInfo : retrieveAllInfoChunksByOwnerName) {
			if (fileInfo.getChunkNumber().equals(chunkNumber)) {
				throw new BadRequestException(
						AppConfiguration.get("bad.same_chunk_uploaded"));
			}
		}
	}
	
	private void validateFileExists(List<FileInfo> infoChunks) {
		if (infoChunks.size() == 0) {
			throw new BadRequestException(
					AppConfiguration.get("bad.file_not_found"));
		}
	}
	
	private void validateIsReadyToDownload(List<FileInfo> infoChunks) {
		if (!(infoChunks.size() == infoChunks.get(0).getAmountOfChunks())) {
			throw new BadRequestException(
					AppConfiguration.get("bad.file_is_not_ready_for_download"));
		}
	}
}
