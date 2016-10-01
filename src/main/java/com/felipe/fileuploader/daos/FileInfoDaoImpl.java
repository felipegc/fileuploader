package com.felipe.fileuploader.daos;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.stream.CloseableUtil;

public class FileInfoDaoImpl extends GenericDaoImpl<FileInfo, String> implements
		FileInfoDao {

	public static final String DATA_BASE_FILE_NAME = "fileInfo.db";

	public FileInfoDaoImpl() {
		super(DATA_BASE_FILE_NAME);
	}

	@Override
	public List<FileInfo> findChunksInfoByOwnerName(String owner, String name) {
		String id = owner + name;
		List<FileInfo> infos = listAllChunkInfoOrganizedById().get(id);
		
		if(infos == null){
			return new ArrayList<>();
		}
		
		return infos;
	}
	
	@Override
	public Map<String,List<FileInfo>> listAllChunkInfoOrganizedById(){
		if (!new File(getDataBasePath()).exists()) {
			return new HashMap<>();
		}
		
		Map<String, List<FileInfo>> infos = new HashMap<>();

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(getDataBasePath());
			ois = new ObjectInputStream(fis);
			
			while (true) {
				FileInfo objRead = (FileInfo) ois.readObject();
				if (infos.get(objRead.getId()) == null) {
					List<FileInfo> chunks = new LinkedList<>();
					chunks.add(objRead);
					infos.put(objRead.getId(), chunks);
				} else {
					infos.get(objRead.getId()).add(objRead);
				}
			}
		} catch (EOFException ignored) {

		} catch (IOException | ClassNotFoundException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.internal_error_message"));
		} finally {
			CloseableUtil.freeOSResources(fis);
			CloseableUtil.freeOSResources(ois);
		}

		return infos;
	}
}
