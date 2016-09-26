package com.felipe.fileuploader.endpoint.poc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test")
public class Test implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;

	public Test() {
	}

	public Test(int id, String name) {
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
