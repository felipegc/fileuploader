package com.felipe.fileuploader.daos;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.felipe.fileuploader.entities.Entity;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.DirUtil;

public class GenericDaoImpl<T extends Entity<I>, I extends Serializable>
		implements GenericDao<T, I> {
	
	private String pathBaseName;
	
	public GenericDaoImpl(String dataBaseName){
		this.pathBaseName = DirUtil.getDirDataBase()
				+ dataBaseName;
	}
	
	@Override
	public Boolean save(T entity) {
		
		Boolean success = true;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {
			fout = new FileOutputStream(pathBaseName, true);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(entity);
		} catch (IOException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.entity_not_saved", entity.getClass()));
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	@Override
	public List<T> findAll() {
		List<T> entities = new LinkedList<>();

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(pathBaseName);
			while (true) {
				ObjectInputStream ois = new ObjectInputStream(fis);
				entities.add((T) ois.readObject());
			}
		} catch (EOFException ignored) {
			//end of file. That's fine. Keep going.
		} catch (IOException | ClassNotFoundException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.entities_not_fetched"));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return entities;
	}

	@Override
	public T findById(I id) {
		Map<I, T> infos = new HashMap<>();

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(pathBaseName);
			while (true) {
				ObjectInputStream ois = new ObjectInputStream(fis);
				T objRead = (T) ois.readObject();
				infos.put(objRead.getId(), objRead);
			}
		} catch (EOFException ignored) {

		} catch (IOException | ClassNotFoundException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.entity_not_fetched", id.toString()));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return infos.get(id);
	}
	
	public String getDataBasePath(){
		return pathBaseName;
	}
}
