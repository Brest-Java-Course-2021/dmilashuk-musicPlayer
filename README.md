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
## Local tests with mysql and kafka
From the same directory as your root pom.xml, type:
```
docker-compose -f docker/app-mySql-kafka.yml up
```
This starts Zookeeper on [http://localhost:2181](http://localhost:2181).\
This starts three kafka brokers on
[http://localhost:9092](http://localhost:9092), [http://localhost:9093](http://localhost:9093), [http://localhost:9094](http://localhost:9094).\
This starts Tomcat and serves up your rest-app project on [http://localhost:8080](http://localhost:8080).\
This starts Tomcat and serves up your kafka-consumer-dao project on [http://localhost:8095](http://localhost:8095).\
This starts Tomcat and serves up your web-app project on [http://localhost:8090](http://localhost:8090).\
```
docker-compose -f docker/app-mySql-kafka.yml down
```
TThis stop all servers and delete containers.

