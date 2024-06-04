FROM openjdk:17-jdk-slim

ARG APP_VERSION

COPY target/salary-service-${APP_VERSION}.jar /app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]