var csv = require("fast-csv");

var formurlencoded = require('form-urlencoded');

var uri = "http://localhost:8080/pipeline_scorer/pipeline/pymes_sm"
 

var request = require('request');

var sleep = require('system-sleep');

var fs = require('fs');

fs.writeFile('eval-scored.csv', '"id","score","error_msg"\n');

csv.fromPath("eval.csv", {
	headers : true
}).on("data", function(data) {
	var formData = formurlencoded(data)
	var contentLength = formData.length;

	request({
		headers : {
			'Content-Length' : contentLength,
			'Content-Type' : 'application/x-www-form-urlencoded'
		},
		uri : uri,
		body : formData,
		//qs: data,
		method : 'POST'
	}, function(err, res, data) {

		try {

			data = JSON.parse(data);			
			if (typeof data.error_msg != 'undefined'){
				
			}			
			fs.appendFile('eval-scored.csv', '"'+data.id+'","'+data.score+'","'+data.error_msg+'"\n', function (err) {
			  if (err) {
			    console.log('Error');
			  } else{
			    console.log(new Date(Date.now()).toLocaleString(),' data:', JSON.stringify(data));
			  }
			});

		}
		catch(err) {
			console.log(new Date(Date.now()).toLocaleString(),'Error');
		}
	});

	sleep(1);
	//process.exit();

}).on("end", function() {
	//console.log("End");
});
