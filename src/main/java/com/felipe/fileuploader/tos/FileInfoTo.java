package com.felipe.fileuploader.tos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileInfoTo implements To {
	
	private String id;
	private String name;
	private String owner;
	private Integer chunkNumber;
	private String status;
	private Long finalTimestamp;

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

	public Integer getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(Integer chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getFinalTimestamp() {
		return finalTimestamp;
	}

	public void setFinalTimestamp(Long finalTimestamp) {
		this.finalTimestamp = finalTimestamp;
	}

}
