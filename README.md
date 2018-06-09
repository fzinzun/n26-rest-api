# REST API
This project create REST API with two entry points to create stats about transactions.

//TODO
Add description about the architecture

## Clone the project
Clone the code in your local machine with the below command and then change the directory to the project
```
$ git clone https://github.com/fzinzun/n26-rest-api.git
$ cd n26-rest-api
```

## Inialize the project
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
Run the test by ....
### Create cobertura reports
//TODO

# Docker lover?
## Compile with docker
//TODO
## Run jar inside container
//TODO
## Run stress test
//TODO
