package com.felipe.fileuploader.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.ws.rs.InternalServerErrorException;

public class DirUtil {

	public static String getDirDataBase() {
		return AppConfiguration.get("files.storage");
	}

	public static String createDirForChunksIfNotExist(String owner, String name) {
		String dir = getDirDataBase() + owner + getSlashUsed() + name.split("\\.")[0]
				+ getSlashUsed();
		if (new File(dir).exists()) {
			return dir;
		}
		new File(dir).mkdirs();
		return dir;
	}
	
	public static String getSlashUsed(){
		return "/";
	}
	
	public static void freeOSResources(FileOutputStream fout) {
		if (fout != null) {
			try {
				fout.flush();
				fout.close();
			} catch (IOException e) {
				throw new InternalServerErrorException(
						AppConfiguration.get("error.internal_error_message"));
			}
		}
	}

	public static void freeOSResources(ObjectOutputStream oos) {
		if (oos != null) {
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				throw new InternalServerErrorException(
						AppConfiguration.get("error.internal_error_message"));
			}
		}
	}
	
	public static void freeOSResources(FileInputStream fis) {
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void freeOSResources(ObjectInputStream ois) {
		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void freeOSResources(BufferedOutputStream bos) {
		if (bos != null) {
			try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				//TODO felipegc throw the server error so endpoint will take them at most.
				e.printStackTrace();
			}
		}
	}
}
