# REST API
This project create REST API with two entry points to create stats about transactions.

The solutions is implemented in java using spring boot 2.0.2 and java 1.8.

```
               +----------+-------+  +-----------+
/transactions  |          |       |  | Thread 2  |
 +------------>+          |       |  +-----------+
               |          |       |  |           |
               |Controller|Service|  |Calculation|
               |          |       |  |           |
/statistics    |          |    <--+--+-->        |
 +------------>+          |    Sync Queue        |
               |          |       |  |           |
               +----------+--------  +-----------+
```
* Statistics Controller
This controller is a RESTController and maps POST /transactions and GET /statistics. This controller call the functions of the statistics service.

* Statistics Service
Following the good practices any logic is implemented in the controller and in this service we check if the transaction received is in the tolerance of 60 seconds. If the message is in the timeframe of 60 seconds we use a Synchronize Queue to share the transaction object with a ApplicationRunner that is processing the files in the backend.

* Calculation
This class implements ApplicationRunner which means spring-boot starts a new thread after the context is created. Numbers ***are not*** formatted or truncated.

## Clone the project
Clone the code in your local machine with the below command and then change the directory to the project
```
$ git clone https://github.com/fzinzun/n26-rest-api.git
$ cd n26-rest-api
```

## Initialize the project
Download all the dependencies with maven.
```
$ mvn clean install
```

## Create eclipse project
In order to update the code in eclipse run the below command and then **import** the project in your eclipse.
```
$ mvn clean eclipse:eclipse
```

## Run the project
Spring-boot has multiple ways to start the application. We are going to use spring-boot plugin.
```
$ mvn spring-boot:run
```

## Build the jar
Run the below command to create the jar file.
```
$ mvn clean package
```

## Testing
This project has junit test and stress test
### Java test
In order to runt the java test run the below command.
```
mvn clean test
```
### Stress test
The stress analysis are created with ***ab*** which is a tool for benchmarking your Apache Hypertext Transfer Protocol.
To run run those tests make sure you have the below requirements.

* Apache ab command installed in your machine
* jq tool to update json in command line (https://stedolan.github.io/jq/)[https://stedolan.github.io/jq/]
* You need bash shell (Linux or MacOS)
* Start the project in the port 8080 as mentioned in the step **Run the project**

1.- Open a **Terminal 1** and move to scripts folder, then starts the monitor script which is going to make a GET call to the **/statistics** every 0.1 seconds.
```
$ cd  /n26-rest-api/src/test/scripts
$ ./listeningStatistic.sh
```
You should see a constant output like this, till you start the second terminal. Leave this terminal in a place where you can see later.  
```
{"sum":0.0,"avg":0.0,"max":0.0,"min":0.0,"count":0}
...
{"sum":0.0,"avg":0.0,"max":0.0,"min":0.0,"count":0}
```

2.- Open a **Terminal 2** and move to scripts folder, then starts **sendTransactions.sh** which is going to create 2000 transactions using 10 clients concurrently. This process will be executed 7 times. That means some point the process is going to have **14000 transactions**.
```
$ cd  /n26-rest-api/src/test/scripts
$ ./sendTransactions.sh
```

You will see some output similar as this where you can find the value ** Time taken for tests ** where is normally constant independent of the transaction processed.
```
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/
......

Concurrency Level:      10
Time taken for tests:   0.395 seconds
Complete requests:      2000
Failed requests:        0
Total transferred:      184000 bytes
Total body sent:        392000
HTML transferred:       0 bytes
Requests per second:    5063.52 [#/sec] (mean)
Time per request:       1.975 [ms] (mean)
Time per request:       0.197 [ms] (mean, across all concurrent requests)
Transfer rate:          454.93 [Kbytes/sec] received
....
```
Going back again to the **Terminal 1** you will see how the statistics numbers change.
```
{"sum":708000.0,"avg":50.57142857142857,"max":94.0,"min":11.0,"count":14000}
{"sum":708000.0,"avg":50.57142857142857,"max":94.0,"min":11.0,"count":14000}
```



### Create cobertura reports
The report of cobertura indicate the porcentage of coverity with test. In this project for the time pressure we have a very low level of coverage.
```
mvn clean cobertura:cobertura
```
Open the HTML report in
```
n26-rest-api/target/site/cobertura/index.html
```

# Docker lover?
As a fan of docker I strongly recommend to use docker to build and run project and the test so we avoid any possible conflict with dependencies or java version.

## Compile with docker
This process will take few minutes but at the end it create the jar file.
```
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean install
```

## Run jar inside container
The below command is similar as the previous the unique difference is that we bind the port **8080** this is use full you have another process using this port. 
```
docker run -it --rm -p 8080:8080 -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.3-jdk-8 mvn spring-boot:run
```
