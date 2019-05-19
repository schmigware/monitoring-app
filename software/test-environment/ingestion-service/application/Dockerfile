FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/ingestion-service-application-0.0.1-SNAPSHOT.jar /app/lib/microservice.jar
ENTRYPOINT ["java","-jar","/app/lib/microservice.jar"]
