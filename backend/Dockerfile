FROM maven:3.8.6-jdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package "-Dquarkus.package.type=uber-jar"

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*-runner.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
