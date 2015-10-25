package com.jyuan92.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jyuan92.twitter.TwitterGet;;

public class TwitterGetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TwitterGet twitterGet = TwitterGet.getInstance();

	/**
	 * Used to fetch real time twitter data from Twitter
	 * Notice: once triggered, it will never stop
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		twitterGet.getTweets();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
