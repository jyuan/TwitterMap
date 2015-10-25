package com.jyuan92.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtil {

	private static final String PROPERTIES_NAME = "config.properties";
	private static final String PROPERTIES_NOT_FOUND = "property file '" + PROPERTIES_NAME
			+ "' not found in the classpath";

	private DatabaseUtil() {
	}

	private static class Holder {
		private static final DatabaseUtil databaseUtil = new DatabaseUtil();
	}

	public static DatabaseUtil getInstance() {
		return Holder.databaseUtil;
	}

	public Connection getConnection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String jdbcUrl = getJDBCUrl();
		Connection connection = DriverManager.getConnection(jdbcUrl);
		return connection;
	}

	public void releaseConnection(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public void releaseStatement(Statement statement) throws SQLException {
		if (statement != null) {
			statement.close();
			statement = null;
		}
	}

	public void releaseResultSet(ResultSet resultSet) throws SQLException {
		if (resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
	}

	private String getJDBCUrl() {
		String jdbcUrl = null;
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_NAME);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException(PROPERTIES_NOT_FOUND);
			}
			// get the property value
			String dbName = prop.getProperty("db_Name");
			String userName = prop.getProperty("db_userName");
			String password = prop.getProperty("db_password");
			String hostname = prop.getProperty("db_hostname");
			String port = prop.getProperty("db_port");
			jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password="
					+ password;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jdbcUrl;
	}
}
