FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/sqlitedemo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]
