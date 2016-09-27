package com.felipe.fileuploader.services;

import java.io.Serializable;
import java.util.List;

import com.felipe.fileuploader.entities.Entity;

public interface GenericService<T extends Entity<I>, I extends Serializable> {
	public Boolean save(T entity);

	public List<T> findAll();

	public T findById(I id);
}
