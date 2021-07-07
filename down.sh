#!/usr/bin/env bash
docker-compose -f docker/app-mySql.yml down
docker rmi docker-rest-app docker-web-app
sudo rm -R docker/mySql_data
mvn clean