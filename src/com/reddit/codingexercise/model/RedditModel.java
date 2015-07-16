package com.reddit.codingexercise.model;

public class RedditModel {

	private String title, thumbnailUrl, largeUrl;

	public RedditModel() {

	}

	public RedditModel(String title, String thumbnailUrl, String largeUrl) {
		super();
		this.title = title;
		this.thumbnailUrl = thumbnailUrl;
		this.largeUrl = largeUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getLargeUrl() {
		return largeUrl;
	}

	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}

}
