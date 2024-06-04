FROM openjdk:17-jdk-slim

ARG APP_VERSION

COPY target/document-service-${APP_VERSION}.jar /app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]