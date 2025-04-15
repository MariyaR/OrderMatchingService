FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/OrderMatchingService-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
