package com.felipe.fileuploader.daos;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, I extends Serializable> {

	public Boolean save(T entity);

	public List<T> findAll();

	public T findById(I id);
	
	public String getDataBasePath();
}
