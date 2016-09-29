package com.felipe.fileuploader.util;

import java.io.File;

import javax.ws.rs.InternalServerErrorException;

public class DirUtil {

	public static String getDirDataBase(){
		return AppConfiguration.get("files.storage");
	}
	
	public static String createDirForChunksIfNotExist(String owner, String name) {
		String dir = getDirDataBase() + owner + "/" + name.split("\\.")[0] + "/";
		if (new File(dir).exists()) {
			return dir;
		}
		boolean mkdir = new File(dir).mkdirs();
		if (!mkdir) {
			throw new InternalServerErrorException(
					AppConfiguration.get("error.repository_not_created"));
		}
		return dir;
	}
}
