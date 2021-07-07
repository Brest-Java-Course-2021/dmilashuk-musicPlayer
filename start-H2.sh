#!/usr/bin/env bash
mvn clean install
docker-compose -f docker/app-H2.yml up