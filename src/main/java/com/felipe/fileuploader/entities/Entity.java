package com.felipe.fileuploader.entities;

import java.io.Serializable;

public abstract class Entity<I extends Serializable> implements
		Serializable {

	public abstract I getId();

	public abstract void setId(I id);
}
