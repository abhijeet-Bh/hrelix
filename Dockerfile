# Use a base image that has Java installed
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file (from the target directory after building the Spring Boot app)
COPY target/hrelix-0.0.1-SNAPSHOT.jar /app/hrelix-0.0.1-SNAPSHOT.jar

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "hrelix-0.0.1-SNAPSHOT.jar"]