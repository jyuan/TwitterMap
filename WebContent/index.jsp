<head>
  <title>Twitter Heatmap</title>
  	<style>
	    html, body, #map-canvas {
	      height: 100%;
	      margin: 0px;
	      padding: 0px;
				font-family: 'Raleway', sans-serif;
				z-index:1;
	    }
  
		#draggable {
			z-index:100; 
			background-color: rgba(200,200,255,.7); 
			width: 250px;
			padding: 20px;
			position:absolute;
			top:10px;
			left:100px;
			cursor: move;
			border: black 1px solid;
		}
		#radius-label, #opacity-label {
			margin-top: 10px;
		}
		#radius-slider, #opacity-slider {
			width:250px;
			margin-top: 10px;
		}
		#project {
			font-size: 10pt;
			font-weight: bold;
			margin-bottom: 10px;
		}
		#radius-slider .ui-slider-handle, 
		#opacity-slider .ui-slider-handle {
			cursor:pointer;
		}
  </style>  
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
  <script src="./jquery-2.1.1.min.js"></script>
  <script src="./heatmap.js"></script>
  <script src="https://code.jquery.com/ui/1.11.0/jquery-ui.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=visualization"></script>
</head>

<body>
  <div id="map-canvas"> </div>

  <div style="text-align: center;" id="draggable">
    <div id="project"><a href="https://github.com/jyuan/TwitterMap" target="X">github.com</a> / jyuan / TwitterMap</div>
		
	<h3>Keyword</h3>
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
	
	<h3>Choice</h3>
	<div id="radius-label">radius: 20</div>
	<div id="radius-slider"></div>

	<div id="opacity-label">opacity: 1</div>
	<div id="opacity-slider"></div>
  </div>
</body>