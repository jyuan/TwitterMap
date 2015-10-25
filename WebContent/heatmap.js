var interval = 30000;
var map, heatmap;
var url = window.location.href+ "api/get/twitter";
var twitter, musicTwitter, newsTwitter, jobTwitter, movieTwitter, 
	androidTwitter, amazonTwitter, gameTwitter, halloweenTwitter;

function addDataIntoMap(url) {
	$.getJSON(url, function(data) {
		console.log(data);
		$.each(data['result'], function(index, row) {
			var category = row["category"];
			if (category === "music") {
				musicTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "news") {
				newsTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "job") {
				jobTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "movie") {
				movieTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "android") {
				androidTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "amazon") {
				amazonTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "game") {
				gameTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
			} else if (category === "halloween") {
				halloweenTwitter.push(new google.maps.LatLng(row["lat"], row["lon"]));
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
		zoom: 5,
		center: new google.maps.LatLng(39.496291, -96.830211),
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	loadHeatmap();
}

function loadHeatmap() {
	heatmap = new google.maps.visualization.HeatmapLayer({
		data : twitter,
		radius: $("#radius-slider").slider("value"),
		opacity: $("#opacity-slider").slider("value")
	});
	heatmap.setMap(map);
}

function updateRealTime() {
	musicTwitter = new google.maps.MVCArray([]);
	newsTwitter = new google.maps.MVCArray([]);
	gameTwitter = new google.maps.MVCArray([]);
	jobTwitter = new google.maps.MVCArray([]);
	androidTwitter = new google.maps.MVCArray([]);
	movieTwitter = new google.maps.MVCArray([]);
	amazonTwitter = new google.maps.MVCArray([]);
	halloweenTwitter = new google.maps.MVCArray([]);
	twitter = new google.maps.MVCArray([]);
	addDataIntoMap(url);
}

function startTimer() {
	timer = setInterval(function() {
		updateRealTime();
	}, interval);
}

$(document).ready(function() {
	musicTwitter = new google.maps.MVCArray([]);
	newsTwitter = new google.maps.MVCArray([]);
	gameTwitter = new google.maps.MVCArray([]);
	jobTwitter = new google.maps.MVCArray([]);
	androidTwitter = new google.maps.MVCArray([]);
	movieTwitter = new google.maps.MVCArray([]);
	amazonTwitter = new google.maps.MVCArray([]);
	halloweenTwitter = new google.maps.MVCArray([]);
	twitter = new google.maps.MVCArray([]);
	google.maps.event.addDomListener(window, 'load', initialize);
	addDataIntoMap(url);
	
	startTimer();
	
	$(function() {
		$( "#draggable" ).draggable();
	});
	
	$(function() {
		$( "#radius-slider" ).slider({
			orientation: "horizontal",
			range: "min",
			min: 1,
			max: 50,
			value: 20,
			slide: function(event, ui) {
				$("#radius-label").html("radius: " + ui.value);
				if(heatmap == null) return;
				heatmap.set('radius', ui.value);
			}
		});
		$( "#opacity-slider" ).slider({
			orientation: "horizontal",
			range: "min",
			min: 0,
			max: 100,
			value: 50,
			slide: function(event, ui) {
				$("#opacity-label").html("opacity: " + ui.value/100);
				if(heatmap == null) return;
				heatmap.set('opacity', ui.value/100);
			}
		});
	});
	
	$('#select').change(function() { 
		var category = $(this).children('option:selected').val();
		if (category == "music") {
	    	heatmap.set('data', musicTwitter);
	    } else if (category == "news") {
	    	heatmap.set('data', newsTwitter);
	    } else if (category == "job") {
	    	heatmap.set('data', jobTwitter);
	    } else if (category == "game") {
	    	heatmap.set('data', gameTwitter);
	    } else if (category == "movie") {
	    	heatmap.set('data', movieTwitter);
	    } else if (category == "amazon") {
	    	heatmap.set('data', amazonTwitter);
	    } else if (category == "android") {
	    	heatmap.set('data', androidTwitter);
	    } else if (category == "halloween") {
	    	heatmap.set('data', halloweenTwitter);
	    } else {
	    	heatmap.set('data', twitter);
	    }
	});
});