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
		new File(dir).mkdirs();
		return dir;
	}
}
