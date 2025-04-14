# Use a JDK image to run the Spring Boot application
FROM eclipse-temurin:21-jdk-alpine-3.20

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build context to the container
# Replace "target/your-app.jar" with the actual path to your JAR file
COPY target/OrderMatchingService-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your app will be listening on
EXPOSE 8080

# Run the Spring Boot app in the container
#CMD ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
