# H2O_MOJO_Pipeline_Scorer
H2O's Mojo Model & Pipeline REST web service for real-time scoring

You could publish your Driverless AI Mojo Pipeline or an H2O Open Source Mojo Model as a REST API
  
Download from https://github.com/rubuntu/H2O_MOJO_Pipeline_Scorer/raw/master/target/h2o_scorer.war

### Inspired by
* https://github.com/openscoring/openscoring
* https://github.com/h2oai/app-consumer-loan

### Scripts for generate sample Open Source H2O mojo models
Based on H2O's app-consumer-loan sample
* R  
  https://github.com/rubuntu/H2O_MOJO_Pipeline_Scorer/blob/master/scripts/script.Rmd

* Python  
  https://github.com/rubuntu/H2O_MOJO_Pipeline_Scorer/blob/master/scripts/script.py

## REST API - H2O Open Source Mojo Model
**Parameters format:** *application/x-www-form-urlencoded*  
### Model REST API endpoints:
| HTTP method | Endpoint | Required role(s) | Description |
| ----------- | -------- | ---------------- | ----------- |
| GET | /model/${id} | - | Get the summary of a model |
| POST | /model/${id} | - | Evaluate data in "single prediction" mode |

## REST API - H2O Comercial Driverless AI Mojo Pipeline

## Caveat:
Copy the license file in **src/main/resources/license.sig** and build the project.  
**Note:** The license file of this sample code has already **expired**.   
You should request a Driverless AI 21-Day Free Trial evaluation license at: https://www.h2o.ai/try-driverless-ai/  

To build the war package of the webapp, enter the project root directory and build using Apache Maven::
```
mvn clean install

```
 You will find the *"war file"* at: **/target/h2o_scorer.war**.  
 If you wan to test the API:
```
mvn clean install tomcat7:run
```




**Parameters format:** *application/x-www-form-urlencoded*  
### Pipeline REST API endpoints:
| HTTP method | Endpoint | Required role(s) | Description |
| ----------- | -------- | ---------------- | ----------- |
| GET | /pipeline/${id} | - | Evaluate data in "single prediction" mode |
| POST | /pipeline/${id} | - | Evaluate data in "single prediction" mode |


### Mojo admin REST API endpoints (webdav) :

| HTTP method | Endpoint | Required role(s) | Description |
| ----------- | -------- | ---------------- | ----------- |
| GET | /mojos | admin | List all Mojo files |
| PUT | /mojos/${mojo_id} | admin | Deploy a model/pipeline |
| DELETE | /mojos/${mojo_id} | admin | Undeploy a model/pipeline |
| GET | /mojos/${mojo_id} | admin | Download a model/pipeline as a Mojo file |


### Test with Node.js 
Sample in https://github.com/rubuntu/H2O_MOJO_Pipeline_Scorer/blob/master/scripts/  

Using endpoints: 
* **/model/BadLoanModel** for *mojo model* file: **BadLoanModel.zip** (Binnary Classification H2O Open Source Mojo Model) 
* **/model/InterestRateModel** for *mojo model* file: **InterestRateModel.zip** (Regression H2O Open Source Mojo Model) 
* **/pipeline/BadLoanPipeline** for *mojo pipeline* file: **BadLoanPipeline.mojo** created with **Driverless AI** (Comercial)

```
{ scripts } master » npm update  

{ scripts } master » head data/test.csv
"id","loan_amnt","longest_credit_length","revol_util","emp_length","home_ownership","annual_inc","purpose","addr_state","dti","delinq_2yrs","total_acc","verification_status","term","bad_loan","int_rate"
"1",3000,4,87.5,9,"RENT",48000,"car","CA",5.35,0,4,"verified","36 months","0",18.64
"2",6000,8,37.73,1,"MORTGAGE",84000,"medical","UT",18.44,2,14,"verified","36 months","0",11.71
"3",15000,9,93.9,2,"MORTGAGE",92000,"credit_card","IL",29.44,0,31,"verified","36 months","0",9.91
"4",5000,6,29.3,2,"RENT",24044,"debt_consolidation","OR",11.93,0,16,"verified","36 months","0",8.9
"5",12000,17,21,10,"RENT",62300,"debt_consolidation","NJ",16.7,0,25,"not verified","36 months","0",7.9
"6",4400,7,99,10,"RENT",55000,"debt_consolidation","RI",20.01,0,11,"not verified","36 months","0",16.77
"7",12000,11,52.1,1,"RENT",46000,"credit_card","TX",8.11,0,12,"not verified","36 months","0",9.91
"8",21000,13,97.6,7,"RENT",50000,"debt_consolidation","WA",21.58,0,14,"verified","60 months","1",19.91
"9",10000,11,59.1,2,"RENT",51400,"credit_card","TX",19.14,0,24,"not verified","36 months","0",10.65  

{ scripts } master » node test.js > test.out

{ scripts } master » head test.out
2018-09-24 16:46:40 request data: {"id":"1","loan_amnt":"3000","longest_credit_length":"4","revol_util":"87.5","emp_length":"9","home_ownership":"RENT","annual_inc":"48000","purpose":"car","addr_state":"CA","dti":"5.35","delinq_2yrs":"0","total_acc":"4","verification_status":"verified","term":"36 months","bad_loan":"0","int_rate":"18.64"}
2018-09-24 16:46:40 request data: {"id":"1","loan_amnt":"3000","longest_credit_length":"4","revol_util":"87.5","emp_length":"9","home_ownership":"RENT","annual_inc":"48000","purpose":"car","addr_state":"CA","dti":"5.35","delinq_2yrs":"0","total_acc":"4","verification_status":"verified","term":"36 months","bad_loan":"0","int_rate":"18.64"}
2018-09-24 16:46:40 request data: {"id":"2","loan_amnt":"6000","longest_credit_length":"8","revol_util":"37.73","emp_length":"1","home_ownership":"MORTGAGE","annual_inc":"84000","purpose":"medical","addr_state":"UT","dti":"18.44","delinq_2yrs":"2","total_acc":"14","verification_status":"verified","term":"36 months","bad_loan":"0","int_rate":"11.71"}
2018-09-24 16:46:40 request data: {"id":"3","loan_amnt":"15000","longest_credit_length":"9","revol_util":"93.9","emp_length":"2","home_ownership":"MORTGAGE","annual_inc":"92000","purpose":"credit_card","addr_state":"IL","dti":"29.44","delinq_2yrs":"0","total_acc":"31","verification_status":"verified","term":"36 months","bad_loan":"0","int_rate":"9.91"}
2018-09-24 16:46:40 request data: {"id":"4","loan_amnt":"5000","longest_credit_length":"6","revol_util":"29.3","emp_length":"2","home_ownership":"RENT","annual_inc":"24044","purpose":"debt_consolidation","addr_state":"OR","dti":"11.93","delinq_2yrs":"0","total_acc":"16","verification_status":"verified","term":"36 months","bad_loan":"0","int_rate":"8.9"}
2018-09-24 16:46:40 request data: {"id":"5","loan_amnt":"12000","longest_credit_length":"17","revol_util":"21","emp_length":"10","home_ownership":"RENT","annual_inc":"62300","purpose":"debt_consolidation","addr_state":"NJ","dti":"16.7","delinq_2yrs":"0","total_acc":"25","verification_status":"not verified","term":"36 months","bad_loan":"0","int_rate":"7.9"}
2018-09-24 16:46:40 request data: {"id":"6","loan_amnt":"4400","longest_credit_length":"7","revol_util":"99","emp_length":"10","home_ownership":"RENT","annual_inc":"55000","purpose":"debt_consolidation","addr_state":"RI","dti":"20.01","delinq_2yrs":"0","total_acc":"11","verification_status":"not verified","term":"36 months","bad_loan":"0","int_rate":"16.77"}
2018-09-24 16:46:40 request data: {"id":"7","loan_amnt":"12000","longest_credit_length":"11","revol_util":"52.1","emp_length":"1","home_ownership":"RENT","annual_inc":"46000","purpose":"credit_card","addr_state":"TX","dti":"8.11","delinq_2yrs":"0","total_acc":"12","verification_status":"not verified","term":"36 months","bad_loan":"0","int_rate":"9.91"}
2018-09-24 16:46:40 request data: {"id":"8","loan_amnt":"21000","longest_credit_length":"13","revol_util":"97.6","emp_length":"7","home_ownership":"RENT","annual_inc":"50000","purpose":"debt_consolidation","addr_state":"WA","dti":"21.58","delinq_2yrs":"0","total_acc":"14","verification_status":"verified","term":"60 months","bad_loan":"1","int_rate":"19.91"}
2018-09-24 16:46:40 request data: {"id":"9","loan_amnt":"10000","longest_credit_length":"11","revol_util":"59.1","emp_length":"2","home_ownership":"RENT","annual_inc":"51400","purpose":"credit_card","addr_state":"TX","dti":"19.14","delinq_2yrs":"0","total_acc":"24","verification_status":"not verified","term":"36 months","bad_loan":"0","int_rate":"10.65"}

```

### Sample output for H2O Open Source Mojo Model Scoring

```
{ scripts } master » head data/test-bad-loan-model-scored.csv
"id","score","error_msg"
"1","0.1897298510715943",""
"2","0.17350490604432564",""
"3","0.19719081594132107",""
"4","0.14846894413726647",""
"5","0.10288972773256007",""
"6","0.19319777100498786",""
"7","0.09573823583793191",""
"8","0.5352944977424021",""
"9","0.11359070256968032",""

{ scripts } master » head data/test-interest-rate-model-scored.csv  
"id","score","error_msg"
"14","13.333684480533787",""
"26","16.34941213094616",""
"15","13.090433142742125",""
"6","15.444714245938751",""
"33","11.65121271203979",""
"19","13.069033750606916",""
"2","15.501789328790704",""
"10","15.549175506018667",""
"28","13.710989034251991",""

```

### Sample output for (Comercial) H2O Driverless AI Mojo Pipeline Scoring

```
{ scripts } master » head data/test-bad-loan-pipeline-scored.csv 
"id","score","error_msg"
"21","0.48449064330499503",""
"114","0.483185094866397",""
"12","0.46887568037781213",""
"19","0.483185094866397",""
"1","0.24140705634889692",""
"18","0.49957621698520194",""
"13","0.05329164212861754",""
"92","0.2688842131348015",""
"49","0.09700626043020003",""

```


### CAVEAT: If your license for Comercial H2O Driverless AI Mojo Pipeline Scoring expired, you will get the error message: "The license expired on..."

```
{ scripts } master » head data/test-bad-loan-pipeline-scored.csv
"id","score","error_msg"
"undefined","undefined","The license expired on Fri Sep 21 00:00:00 PYT 2018"
"undefined","undefined","The license expired on Fri Sep 21 00:00:00 PYT 2018"
"undefined","undefined","The license expired on Fri Sep 21 00:00:00 PYT 2018"
```


### PS:
Please take a look at [Villu Ruusmann's](https://github.com/vruusmann) awesome project [JPMML-H2O](https://github.com/jpmml/jpmml-h2o) : A Java library and command-line application for converting **H2O.ai** models to **PMML**.
