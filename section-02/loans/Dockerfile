# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only necessary files first to leverage Docker layer caching
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 2: Run the application with a minimal image
FROM openjdk:21-slim

# Create app directory
WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/target/loans-0.0.1-SNAPSHOT.jar loans.jar

# Expose the default Spring Boot port
EXPOSE 8090

# Run the app
ENTRYPOINT ["java", "-jar", "loans.jar"]