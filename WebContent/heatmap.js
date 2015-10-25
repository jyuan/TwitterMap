var map, heatmap;
var csv = [];
var twitter;
var url = window.location.href+ "api/get/twitter";
var music, news, job;

function addDataIntoMap(url) {
	$.getJSON(url, function(data) {
		console.log(data);
		$.each(data['result'], function(index, row) {
			var category = row["category"];
			if (category === "music") {
				music.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "news") {
				news.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "job") {
				job.push(new google.maps.LatLng(row["lat"], row["lon"]));
			}
			twitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
		});
	});
}

function toggleHeatmap() {
	heatmap.setMap(heatmap.getMap() ? null : map);
}

function initialize() {
	var mapOptions = {
		zoom : 3,
		center : new google.maps.LatLng(39.496291, -96.830211),
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	loadHeatmap();
}

function loadHeatmap() {
	music = new google.maps.MVCArray([]);
	news = new google.maps.MVCArray([]);
	job = new google.maps.MVCArray([]);
	twitter = new google.maps.MVCArray([]);
	heatmap = new google.maps.visualization.HeatmapLayer({
		data : twitter,
		radius : 20,
		opacity : 1,
	});
	heatmap.setMap(map);
}

$(document).ready(function() {
	google.maps.event.addDomListener(window, 'load', initialize);
	addDataIntoMap(url);
	
	$('#select').change(function() { 
		var category = $(this).children('option:selected').val();
		if (category == "music") {
	    	heatmap.set('data', music);
	    } else if (category == "news") {
	    	heatmap.set('data', news);
	    } else if (category == "job") {
	    	heatmap.set('data', job);
	    } else {
	    	heatmap.set('data', twitter);
	    }
	});
});