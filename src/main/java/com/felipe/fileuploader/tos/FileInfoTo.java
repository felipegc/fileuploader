package com.felipe.fileuploader.tos;

import javax.xml.bind.annotation.XmlRootElement;

import com.felipe.fileuploader.enums.StatusUpload;

@XmlRootElement
public class FileInfoTo implements To {
	
	private String id;
	private String name;
	private String owner;
	private Integer numberOfChunks;
	private StatusUpload status;
	private Long minutesSpent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getNumberOfChunks() {
		return numberOfChunks;
	}

	public void setNumberOfChunks(Integer numberOfChunks) {
		this.numberOfChunks = numberOfChunks;
	}

	public StatusUpload getStatus() {
		return status;
	}

	public void setStatus(StatusUpload status) {
		this.status = status;
	}

	public Long getMinutesSpent() {
		return minutesSpent;
	}

	public void setMinutesSpent(Long minutesSpent) {
		this.minutesSpent = minutesSpent;
	}
}
