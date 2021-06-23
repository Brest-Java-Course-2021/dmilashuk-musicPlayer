[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/dmilashuk-musicPlayer/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/dmilashuk-musicPlayer/actions/workflows/maven.yml)

# Music player test project

Technical task: [./documentation/Technical task.md](./documentation/Technical task.md).

Database structure: [./documentation/Database structure.pdf](./documentation/Database structure.pdf).
## Requirements

* JDK 11
* Apache Maven
* Docker 20.10.6
* Docker-compose 1.29.1

## Build application:
```
mvn clean install
```
## Local tests with H2 in-memory
From the same directory as your root pom.xml, type:
```
java -jar rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
This starts Tomcat and serves up your rest-app project on [http://localhost:8080](http://localhost:8080).
```
java -jar web-app/target/web-app-1.0-SNAPSHOT.jar 
```
This starts Tomcat and serves up your web-app project on [http://localhost:8090](http://localhost:8090).
### It is possible to run openapi generated rest-app-openapi instead rest-app with next command:
```
java -jar rest-app-openapi/target/rest-app-openapi-1.0-SNAPSHOT.jar 
```
This starts Tomcat and serves up your rest-app project on [http://localhost:8080](http://localhost:8080).
## Run application with others databases
Mysql require(can be customized in application-mysql.properties file): 
* database - musicPlayer
* user - root
* password - password \
Run with mysql:
```
java -jar -Dspring.profiles.active=mySql rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
Postgres require(can be customized in application-postgres.properties file): 
* database - musicPlayer
* user - postgres
* password - password \
Run with postgres:
```
java -jar -Dspring.profiles.active=postgres rest-app/target/rest-app-1.0-SNAPSHOT.jar
```

## Run application with docker-compose and mySql database
From the same directory as your root pom.xml, type:
```
docker-compose -f docker/app-mySql.yml up
```
The mySql database can be accessed at: [http://localhost:3306](http://localhost:3306)\
The rest-app can be accessed at: [http://localhost:8080](http://localhost:8080)\
The web-app can be accessed at: [http://localhost:8090](http://localhost:8090)
 
 To start/stop app use:
 ```
 docker-compose -f docker/app-mySql.yml start
 docker-compose -f docker/app-mySql.yml stop
 ```
To stop it and remove the container, run:
 ```
 docker-compose -f docker/app-mySql.yml down
 ```
## Local tests with Postman
You can import postman collection: [./documentation/music player.postman_collection.json](./documentation/music%20player.postman_collection.json).

## Swagger
Swagger json documentation can be accessed at : [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs) \
Swagger UI can be accessed at : [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)