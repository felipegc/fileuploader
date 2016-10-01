package com.felipe.fileuploader.tos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseErrorTo implements To {

	Integer status;
	String message;

	public ResponseErrorTo() {
	}

	public ResponseErrorTo(Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
