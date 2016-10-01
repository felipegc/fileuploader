package com.felipe.fileuploader.daos;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import com.felipe.fileuploader.entities.Entity;
import com.felipe.fileuploader.util.AppConfiguration;
import com.felipe.fileuploader.util.AppendableObjectOutputStream;
import com.felipe.fileuploader.util.DirUtil;

public class GenericDaoImpl<T extends Entity<I>, I extends Serializable>
		implements GenericDao<T, I> {

	private String pathBaseName;

	public GenericDaoImpl(String dataBaseName) {
		this.pathBaseName = DirUtil.getDirDataBase() + dataBaseName;
	}

	@Override
	public Boolean save(T entity) {

		Boolean success = true;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			//List<T> allEntities = findAll();

			if (new File(getDataBasePath()).exists()) {
				fout = new FileOutputStream(pathBaseName, true);
				oos = new AppendableObjectOutputStream(fout);
			} else {
				fout = new FileOutputStream(pathBaseName);
				oos = new ObjectOutputStream(fout);
			}

			//fout = new FileOutputStream(pathBaseName, true);
			//oos = new AppendableObjectOutputStream(fout);

			// fout = new FileOutputStream(pathBaseName);
			// oos = new ObjectOutputStream(fout);

			// for (T previousEntity : allEntities) {
			// oos.writeObject(previousEntity);
			// }

			oos.writeObject(entity);
			// oos.reset();
		} catch (IOException e) {
			throw new InternalServerErrorException(AppConfiguration.get(
					"error.entity_not_saved", entity.getClass()));
		} finally {
			if (fout != null) {
				try {
					fout.flush();
					fout.close();
				} catch (IOException e) {
					throw new InternalServerErrorException(
							AppConfiguration
									.get("error.internal_error_message"));
				}
			}

			if (oos != null) {
				try {
					oos.flush();
					oos.close();
				} catch (IOException e) {
					throw new InternalServerErrorException(
							AppConfiguration
									.get("error.internal_error_message"));
				}
			}
		}
		return success;
	}

	@Override
	public List<T> findAll() {
		if (!new File(getDataBasePath()).exists()) {
			return new ArrayList<>();
		}

		List<T> entities = new LinkedList<>();

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			// TODO felipegc abri um trem aqui
			fis = new FileInputStream(pathBaseName);
			ois = new ObjectInputStream(fis);
			while (true) {
				entities.add((T) ois.readObject());
			}
		} catch (EOFException ignored) {
			// end of file. That's fine. Keep going.
		} catch (IOException | ClassNotFoundException e) {
			throw new InternalServerErrorException(
					AppConfiguration.get("error.entities_not_fetched"));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return entities;
	}

	@Override
	public T findLastDataSavedById(I id) {
		if (!new File(getDataBasePath()).exists()) {
			return null;
		}

		Map<I, T> infos = new HashMap<>();

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(pathBaseName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			while (true) {
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

	public String getDataBasePath() {
		return pathBaseName;
	}
}
