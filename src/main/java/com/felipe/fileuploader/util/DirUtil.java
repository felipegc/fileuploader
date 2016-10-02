package com.felipe.fileuploader.util;

import java.io.File;

public class DirUtil {

	public static String getDirDataBase() {
		return AppConfiguration.get("files.storage");
	}

	public static String createDirForChunksIfNotExist(String owner, String name) {
		String dir = getDirByOwnerAndFileName(owner, name);
		if (new File(dir).exists()) {
			return dir;
		}
		new File(dir).mkdirs();
		return dir;
	}

	public static String getDirByOwnerAndFileName(String owner, String fileName) {
		String dir = getDirDataBase() + owner + getSlashUsed()
				+ fileName.split("\\.")[0] + getSlashUsed();
		
		return dir;
	}

	public static String getSlashUsed() {
		return File.separator;
	}
}
