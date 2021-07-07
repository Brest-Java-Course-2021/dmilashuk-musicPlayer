#!/usr/bin/env bash
mvn clean install
docker-compose -f docker/app-mySql.yml up
