package com.jyuan92.twitter;

public class Twitter {

	private final long twitterID;
	private final String username;
	private final Double latitude;
	private final Double longitude;
	private final String content;
	private final long timestamp;
	private final String category;

	public Twitter(long twitterID, String username, Double latitude, Double longitude, 
			String content, long timestamp, String category) {
		this.twitterID = twitterID;
		this.username = username;
		this.latitude = latitude;
		this.longitude = longitude;
		this.content = content;
		this.timestamp = timestamp;
		this.category = category;
	}

	public long getTwitterID() {
		return twitterID;
	}

	public String getUsername() {
		return username;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getContent() {
		return content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getCategory() {
		return category;
	}
}
