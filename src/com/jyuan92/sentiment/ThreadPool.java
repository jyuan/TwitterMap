package com.jyuan92.sentiment;

import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.jyuan92.aws.SNSServiceHandler;
import com.jyuan92.aws.SQSServiceHandler;

public class ThreadPool implements Runnable, Serializable {
	
	private static final String PREFIX_URL = "http://access.alchemyapi.com/calls/text/TextGetTextSentiment?apikey=";
	private static final String MIDFIX_URL = "&&sentiment=1&showSourceText=1&text=";
	private static final String POSTFIX_URL = "&outputMode=json";
	private static final String PROPERTIES_NAME = "config.properties";
	private static final String PROPERTIES_NOT_FOUND = "property file '" + PROPERTIES_NAME
			+ "' not found in the classpath";
	private static final String NOT_PARSE = "not parse: -9999";

	private static final long serialVersionUID = 0;

	public ThreadPool() { }

	@Override
	public void run() {
		SQSServiceHandler sqsServiceHandler = SQSServiceHandler.getInstance();
		while (true) {
			String message = sqsServiceHandler.getMessage();
			if (null == message) {
				try {
					System.out.println("no message in SQS, wait for 5 seconds");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String[] messages = message.split("\t\t");
			try {
				System.out.println("twitter content: " + messages[3]);
				String score = getScoreFromAlchemyAPI(messages[3]);
				System.out.println("score of the content: " + score);
				SNSServiceHandler snsServiceHandler = SNSServiceHandler.getInstance();
				snsServiceHandler.publishMessage(message);
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getScoreFromAlchemyAPI(String tweet) throws IOException, JSONException {
		String encodeTweet = URLEncoder.encode(tweet, "UTF-8");
		String APIKey = getAlchemyAPIKey();
		if (null == APIKey) {
			throw new FileNotFoundException(PROPERTIES_NOT_FOUND);
		}
		URL url = new URL(PREFIX_URL + APIKey + MIDFIX_URL + encodeTweet + POSTFIX_URL);
		
		// make connection
		URLConnection urlConnection = url.openConnection();
		// use post mode
		urlConnection.setDoOutput(true);
		urlConnection.setAllowUserInteraction(false);
		// get result
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

		String jsonString = "";
		String line = null;
		
		while ((line = bufferedReader.readLine()) != null) {
			jsonString = jsonString + line + "\n";
		}
		bufferedReader.close();
		
		String res = "";
		JSONObject jsonObject = new JSONObject(jsonString);
		String status = (String) jsonObject.get("status");
		if ("ERROR".equals(status)) {
			return NOT_PARSE;
		} else {
			JSONObject docSentiment = (JSONObject) jsonObject.get("docSentiment");
			String type = (String) docSentiment.get("type");
			res += type + ",";
			String score = (String) docSentiment.get("score");
			if (score != null) {
				res += score;
			} else {
				res += "0";
			}
		}
		return res;
	}
	
	private String getAlchemyAPIKey() {
		Properties prop = new Properties();
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_NAME)) {
			if (inputStream != null) {
				prop.load(inputStream);
			}
			return prop.getProperty("AlchemyAPIKey");
		} catch (IOException e) {
			return null;
		}
	}
}