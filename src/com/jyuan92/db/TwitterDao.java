package com.jyuan92.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jyuan92.twitter.Twitter;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

public class TwitterDao {
	private static final String TABLE_NAME = "twitter";
	private static final String INSERT_TO_TWITTER = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?,?)";
	private static final String GET_GEO_DATA = "SELECT latitude, longitude, category FROM " + TABLE_NAME;
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
			+ "(twitterId LONG, username VARCHAR(50), latitude DOUBLE, longitude DOUBLE,"
			+ " content VARCHAR(200), timestamp LONG, category VARCHAR(20))";

	public boolean insert(Twitter twitter) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			conn = getConnection();
			statement = conn.prepareStatement(INSERT_TO_TWITTER);
			statement.setLong(1, twitter.getTwitterID());
			statement.setString(2, twitter.getUsername());
			statement.setDouble(3, twitter.getLatitude());
			statement.setDouble(4, twitter.getLongitude());
			statement.setString(5, twitter.getContent());
			statement.setLong(6, twitter.getTimestamp());
			statement.setString(7, twitter.getCategory());
			statement.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			releaseDatabase(conn, statement, resultSet);
		}
		return true;
	}

	public JSONArray getAllTweets() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		JSONArray locations = new JSONArray();
		try {
			conn = getConnection();
			statement = conn.prepareStatement(GET_GEO_DATA);
			resultSet = statement.executeQuery();
			// store in JSON
			JSONObject twitterObject;
			while (resultSet.next()) {
				twitterObject = new JSONObject();
				twitterObject.put("lat", resultSet.getDouble("latitude"));
				twitterObject.put("lon", resultSet.getDouble("longitude"));
				twitterObject.put("category", resultSet.getString("category"));
				locations.put(twitterObject);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
				| JSONException e) {
			e.printStackTrace();
		} finally {
			releaseDatabase(conn, statement, resultSet);
		}
		return locations;
	}

	public void checkAndCreateTable(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.executeUpdate(CREATE_TABLE);
	}

	private Connection getConnection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return DatabaseUtil.getInstance().getConnection();
	}

	private void releaseDatabase(Connection conn, PreparedStatement statement, ResultSet resultSet) {
		try {
			DatabaseUtil.getInstance().release(resultSet, statement, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
