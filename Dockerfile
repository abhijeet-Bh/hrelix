# Use the official OpenJDK image to run the app
FROM openjdk:17-jdk

WORKDIR /app
COPY target/*.jar /app.jar
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]