package com.felipe.fileuploader.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.DirUtil;

public class FileServiceImpl {

	FileInfoService fileInfoService = new FileInfoServiceImpl();

	// TODO felipegc remove fileDetail
	public List<FileInfo> uploadFile(Integer chunkNumber, Integer chunksExpected,
			String owner, String name, InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail) {

		try {

			Long initTimestamp = new Date().getTime();

			validateUpload(chunkNumber, chunksExpected, owner, name,
					uploadedInputStream);

			String fileLocation = DirUtil.createDirForChunksIfNotExist(owner,
					name) + name + chunkNumber;

			FileOutputStream out = new FileOutputStream(new File(fileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(fileLocation));
			read = uploadedInputStream.read(bytes);
			while (read != -1) {
				out.write(bytes, 0, read);
				read = uploadedInputStream.read(bytes);
			}

			out.flush();
			out.close();

			Long finalTimestamp = new Date().getTime();

			fileInfoService.saveFileInformation(owner, name, chunkNumber,
					chunksExpected, initTimestamp, finalTimestamp);
			
			return fileInfoService.retrieveAllInfoChunksByOwnerName(owner, name);
		} catch (FileNotFoundException ex) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.directory_not_found",
					AppConfiguration.get("files.storage")));
		} catch (IOException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.chunk_not_saved", chunkNumber, name));
		}
	}

	private void validateUpload(Integer chunkNumber, Integer chunksExpected,
			String owner, String name, InputStream uploadedInputStream) {

		validateMandatoryFields(chunkNumber, chunksExpected, owner, name,
				uploadedInputStream);

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
					"error.mandatory_field_not_present", field));
		}
	}
}
