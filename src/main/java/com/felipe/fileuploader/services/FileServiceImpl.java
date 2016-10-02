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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.DirUtil;
import com.felipe.fileuploader.util.stream.CloseableUtil;

public class FileServiceImpl implements FileService {

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
			CloseableUtil.freeOSResources(out);
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

			String finalFilePath = chunksDir + fileName;
			List<File> chunksToBeMerged = buildListChunksToBeMerged(chunks,
					finalFilePath);

			fileMerged = new File(finalFilePath);
			mergingStream = new BufferedOutputStream(new FileOutputStream(
					new File(finalFilePath), true));

			for (File file : chunksToBeMerged) {
				mergeFile(mergingStream, file);
			}

		} catch (FileNotFoundException ex) {
			throw new InternalServerErrorException(
					AppConfiguration.get("bad.chunk_not_found"));

		} catch (IOException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.file_not_merged", fileName));
		} finally {
			CloseableUtil.freeOSResources(mergingStream);
		}

		return fileMerged;
	}

	private List<File> buildListChunksToBeMerged(List<FileInfo> chunks,
			String fileToBeMerged) throws IOException {
		List<File> chunkFiles = new LinkedList<>();
		Path path = Paths.get(fileToBeMerged);

		if (Files.exists(path)) {
			Files.delete(path);
			chunkFiles.add(new File(fileToBeMerged));
		}
		for (FileInfo fileInfo : chunks) {
			chunkFiles
					.add(new File(fileToBeMerged + fileInfo.getChunkNumber()));

		}
		return chunkFiles;
	}

	private void mergeFile(OutputStream os, File source) throws IOException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(source));
			copyBuffer(is, os);
		} finally {
			CloseableUtil.freeOSResources(is);
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

	private void validateDownload(List<FileInfo> infoChunks) {
		validateFileExists(infoChunks);
		validateIsReadyForDownload(infoChunks);
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

	private void validateIsReadyForDownload(List<FileInfo> infoChunks) {
		if (!StatusUpload.FINISHED.equals(fileInfoService
				.defineStatus(infoChunks))) {
			throw new BadRequestException(
					AppConfiguration.get("bad.file_is_not_ready_for_download"));
		}
	}
}
