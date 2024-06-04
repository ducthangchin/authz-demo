FROM openjdk:17-jdk-slim

ARG APP_VERSION

COPY target/eureka-server-${APP_VERSION}.jar /app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app.jar"]