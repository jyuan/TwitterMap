package com.jyuan92.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jyuan92.db.TwitterDao;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class TwitterFetchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private TwitterDao twitterDAO;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TwitterFetchServlet() {
		super();
		twitterDAO = new TwitterDao();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		JSONObject json = new JSONObject();
		try {
			json.put("result", twitterDAO.getAllTweets());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		res.setContentType("application/json");
		res.getWriter().write(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
