package com.felipe.fileuploader.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileInfo extends Entity<String>{

	private String id;
	private String name;
	private String url;
	private String owner;
	private String status;
	private Integer chunkNumber;
	private Long initTimestamp;
	private Long finalTimestamp;

	public FileInfo() {

	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(Integer chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	public Long getInitTimestamp() {
		return initTimestamp;
	}

	public void setInitTimestamp(Long initTimestamp) {
		this.initTimestamp = initTimestamp;
	}

	public Long getFinalTimestamp() {
		return finalTimestamp;
	}

	public void setFinalTimestamp(Long finalTimestamp) {
		this.finalTimestamp = finalTimestamp;
	}
}
