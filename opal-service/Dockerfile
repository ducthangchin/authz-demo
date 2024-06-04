FROM openjdk:17-jdk-slim

# list all files
ARG APP_VERSION

COPY target/opal-service-1.0-SNAPSHOT.jar /app.jar
RUN ls -la


EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]