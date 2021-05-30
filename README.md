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
## Local tests
From the same directory as your root pom.xml, type:
```
java -jar rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
This starts Tomcat and serves up your rest-app project on [http://localhost:8080](http://localhost:8080).
```
java -jar web-app/target/web-app-1.0-SNAPSHOT.jar 
```
This starts Tomcat and serves up your web-app project on [http://localhost:8090](http://localhost:8090).
## Local tests with Postman
You can import postman collection: [./documentation/music player.postman_collection.json](./documentation/music%20player.postman_collection.json).
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

## Swagger
Swagger json documentation can be accessed at : [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs) \
Swagger UI can be accessed at : [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)