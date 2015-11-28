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
	private final String score;

	public Twitter(long twitterID, String username, Double latitude, Double longitude, String content, long timestamp,
			String category, String score) {
		this.twitterID = twitterID;
		this.username = username;
		this.latitude = latitude;
		this.longitude = longitude;
		this.content = content;
		this.timestamp = timestamp;
		this.category = category;
		this.score = score;
	}

	public long getTwitterID() {
		return this.twitterID;
	}

	public String getUsername() {
		return this.username;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public String getContent() {
		return this.content;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getCategory() {
		return this.category;
	}
	
	public String getScore() {
		return this.score;
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
		return "Twitter [twitterID=" + twitterID + ", username=" + username + ", latitude=" + latitude + ", longitude="
				+ longitude + ", content=" + content + ", timestamp=" + timestamp + ", category=" + category + ", score=" + score + "]";
	}
	
	public JSONObject getJSONObject() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("twitterID", twitterID);
			jsonObject.put("username", username);
			jsonObject.put("latitude", latitude);
			jsonObject.put("longitude", longitude);
			jsonObject.put("content", content);
			jsonObject.put("timestamp", timestamp);
			jsonObject.put("category", category);
			jsonObject.put("score", score);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}		
		return jsonObject;
	}
	
	public String getHeatmapInfo() {
		return twitterID + "\t\t" + latitude + "\t\t" + longitude + "\t\t" + content;
	}

}
