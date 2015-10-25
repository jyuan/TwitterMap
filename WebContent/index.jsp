<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.jyuan92.twitter.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
  <title>Twitter Heatmap</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
  <script src="./jquery-2.1.1.min.js"></script>
  <script src="./heatmap.js"></script>
  <script src="https://code.jquery.com/ui/1.11.0/jquery-ui.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=visualization"></script>
  <style>
    html, body, #map-canvas {
      padding: 0px;
      font-family: 'Raleway', sans-serif;
      margin: 0px;
      width:100%;
      height:100%;
    }
    #top {
      z-index: 2;
    }
  </style>
</head>

<body>
  <div id="map-canvas"> </div>

  <div id="top" style="text-align: center; position: absolute; top: 20px; right: 20px; width: 180px; height: 120px; background: white;">
    <h2>Keyword</h2>
    <select id="select">
	  <option value="all">All Twitter</option>
      <option value="amazon">Amazon</option>
      <option value="android">Android</option>
	  <option value="game">Game</option>
	  <option value="halloween">Halloween</option>
	  <option value="job">Job</option>
	  <option value="movie">Movie</option>
	  <option value="music">Music</option>
	  <option value="news">News</option>
	</select>
  </div>
</body>