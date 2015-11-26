package com.jyuan92.twitter;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class Twitter {

	private final long twitterID;
	private final String username;
	private final Double latitude;
	private final Double longitude;
	private final String content;
	private final long timestamp;
	private final String category;

	public Twitter(long twitterID, String username, Double latitude, Double longitude, String content, long timestamp,
			String category) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (twitterID ^ (twitterID >>> 32));
		return result;
	}

	/**
	 * two twitter are equal if and only if the twetterID is the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Twitter other = (Twitter) obj;
		if (twitterID != other.twitterID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("twitterID", twitterID);
			jsonObject.put("username", username);
			jsonObject.put("latitude", latitude);
			jsonObject.put("longitude", longitude);
			jsonObject.put("content", content);
			jsonObject.put("timestamp", timestamp);
			jsonObject.put("category", category);	
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}		
		return jsonObject.toString();
	}

}
