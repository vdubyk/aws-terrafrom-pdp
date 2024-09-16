#!/bin/bash
set -e

sudo apt update -y
sudo apt install -y openjdk-17-jdk git awscli maven

DB_USERNAME='${db_username}'
DB_PASSWORD='${db_password}'
DB_URL='${db_url}'
ORIGIN_URL='${origin_url}'

export DB_USERNAME
export DB_PASSWORD
export DB_URL
export ORIGIN_URL

git clone https://github.com/vdubyk/aws-terrafrom-pdp.git /home/ubuntu/app-code
cd /home/ubuntu/app-code/backend

mvn clean install

nohup java -jar target/backend-1.0-SNAPSHOT.jar \
  --spring.datasource.url=$DB_URL \
  --spring.datasource.username=$DB_USERNAME \
  --spring.datasource.password=$DB_PASSWORD \
  --spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect \
  --origin.url=$ORIGIN_URL > /home/ubuntu/backend.log 2>&1 &
