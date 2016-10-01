package com.felipe.fileuploader.entities;

import com.felipe.fileuploader.enums.StatusUpload;

public class FileInfo extends Entity<String> implements Comparable<FileInfo> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String url;
	private String owner;
	private StatusUpload status;
	private Integer chunkNumber;
	private Integer amountOfChunks;
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

	public StatusUpload getStatus() {
		return status;
	}

	public void setStatus(StatusUpload status) {
		this.status = status;
	}

	public Integer getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(Integer chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	public Integer getAmountOfChunks() {
		return amountOfChunks;
	}

	public void setAmountOfChunks(Integer amountOfChunks) {
		this.amountOfChunks = amountOfChunks;
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

	@Override
	public int compareTo(FileInfo fileInfo) {
		Integer compNumber1 = this.getChunkNumber();
		Integer compNumber2 = ((FileInfo) fileInfo).getChunkNumber();
		return Integer.compare(compNumber1, compNumber2);
	}
}
