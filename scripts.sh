#!/bin/bash
docker-compose -f deployment.yml down
mvn clean package
docker-compose -f deployment.yml up -d --build
