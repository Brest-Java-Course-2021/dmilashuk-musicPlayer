[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/dmilashuk-musicPlayer/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/dmilashuk-musicPlayer/actions/workflows/maven.yml)

# Music player test project

Technical task: [./documentation/Technical task.md](./documentation/Technical task.md).

Database structure: [./documentation/Database structure.pdf](./documentation/Database structure.pdf).
## Requirements

* JDK 11
* Apache Maven

## Build application:
```
mvn clean install
```
## Local tests with Jetty Maven Plugin
From the same directory as your root pom.xml, type:
```
mvn jetty:run
```
This starts Jetty and serves up your rest-app project on [http://localhost:8080](http://localhost:8080).
```
cd ./web-app; mvn jetty:run
```
This starts Jetty and serves up your web-app project on [http://localhost:8090](http://localhost:8090).
## Local tests with Postman
You can import postman collection: [./documentation/music player.postman_collection.json](./documentation/music%20player.postman_collection.json).