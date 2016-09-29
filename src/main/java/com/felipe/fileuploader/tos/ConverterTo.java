package com.felipe.fileuploader.tos;

import java.util.List;

public interface ConverterTo<E ,T> {

	public abstract T converterFromEntityTo(List<E> chunks);
}
