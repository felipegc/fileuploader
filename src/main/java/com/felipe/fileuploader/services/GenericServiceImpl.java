package com.felipe.fileuploader.services;

import java.io.Serializable;
import java.util.List;

import com.felipe.fileuploader.daos.GenericDao;
import com.felipe.fileuploader.entities.Entity;

public class GenericServiceImpl<T extends Entity<I>, I extends Serializable, D extends GenericDao<T, I>>
		implements GenericService<T, I> {

	protected final D dao;

	public GenericServiceImpl(D dao) {
		this.dao = dao;
	}

	@Override
	public Boolean save(T entity) {
		return dao.save(entity);
	}

	@Override
	public List<T> findAll() {
		return dao.findAll();
	}

	@Override
	public T findLastDataSavedById(I id) {
		return dao.findLastDataSavedById(id);
	}
}
