#!/bin/bash

APP_NAME=accounts-ms
IMAGE_NAME=accounts-service

# Always stop & remove old container
docker rm -f $APP_NAME 2>/dev/null

# Remove image if --clean flag is passed
if [ "$1" == "--clean" ]; then
    echo "🧽 Cleaning image..."
    docker rmi -f $IMAGE_NAME 2>/dev/null
fi

# Build the Spring Boot app JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t $IMAGE_NAME .

# Run the container
docker run -d --name $APP_NAME -p 8080:8080 $IMAGE_NAME

echo "🚀 $APP_NAME is running at http://localhost:8080"
