var csv = require("fast-csv");

var formurlencoded = require('form-urlencoded');

var request = require('request');

var sleep = require('system-sleep');

var fs = require('fs');

function testService(uri, out_file) {

	fs.writeFile(out_file, '"id","score","error_msg"\n');
	
	csv.fromPath("data/test.csv", {
		headers : true
	}).on("data", function(data) {
		var formData = formurlencoded(data)
		var contentLength = formData.length;
		console.log(new Date(Date.now()).toLocaleString(),'request data:', JSON.stringify(data));		
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
				fs.appendFile(out_file, '"'+data.id+'","'+data.score+'","'+data.error_msg+'"\n', function (err) {
				  if (err) {
				    console.log('Error');
				  } else{
				    console.log(new Date(Date.now()).toLocaleString(),'response data:', JSON.stringify(data));
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

}


// Open Source H2O

var uri = "http://localhost:8080/h2o_scorer/model/BadLoanModel";
var out_file = "data/test-bad-loan-model-scored.csv";

testService(uri, out_file);

uri = "http://localhost:8080/h2o_scorer/model/InterestRateModel";
out_file = "data/test-interest-tate-model-scored.csv";

testService(uri, out_file);

// Driverless AI Mojo Pipelines (Comercial)
// Caveat: 
//   Copy licence file to "src/main/resources/license.sig"  
//   Then build and test the war package: mvn clean install tomcat7:run 

// uri = "http://localhost:8080/h2o_scorer/pipeline/BadLoanPipeline";
// out_file = "data/test-bad-loan-pipeline-scored.csv";
// testService(uri, out_file);

// uri = "http://localhost:8080/h2o_scorer/pipeline/InterestRatePipeline";
// out_file = "data/test-interest-tate-pipeline-scored.csv";
// testService(uri, out_file);

