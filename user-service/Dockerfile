FROM openjdk:17-jdk-slim

ARG APP_VERSION

COPY target/user-service-${APP_VERSION}.jar /app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]