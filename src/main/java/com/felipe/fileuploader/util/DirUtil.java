package com.felipe.fileuploader.util;

public class DirUtil {

	public static String getDirDataBase(){
		return AppConfiguration.get("files.storage");
	}
}
