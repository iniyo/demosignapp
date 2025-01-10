# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/demo-0.0.1-SNAPSHOT.jar auth-app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "auth-app.jar"]
