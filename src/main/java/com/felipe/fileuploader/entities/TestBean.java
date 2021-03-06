package com.felipe.fileuploader.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestBean {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;

	public TestBean() {
	}

	public TestBean(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
