FROM openjdk:17-jdk-slim

ARG APP_VERSION

COPY target/api-gateway-${APP_VERSION}.jar /app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app.jar"]