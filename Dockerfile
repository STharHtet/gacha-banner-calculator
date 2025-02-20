# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/banners-0.0.1-SNAPSHOT.jar app.jar

# Copy banners.json to make it available inside the container. This will not persist
COPY banners.json /app/banners.json

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]

