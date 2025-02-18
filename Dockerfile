## Use an official Maven image to build the app
#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#
## Copy the pom.xml and download dependencies
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
## Copy the source code and build the application
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Second stage: use a lightweight JDK image to run the app
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#
## Copy the built jar from the previous stage
#COPY --from=build /app/target/*.jar app.jar
#
## Expose port 8080
#EXPOSE 8080
#
## Run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Use the official OpenJDK image to run the app
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the compiled Spring Boot jar file into the container
COPY target/*.jar /app.jar

# Expose the port your Spring Boot app is running on (usually 8080)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]