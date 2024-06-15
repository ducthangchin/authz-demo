#!/bin/bash
mvn clean package
docker-compose -f deployment.yml up -d --build
