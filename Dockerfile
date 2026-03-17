# Spring Boot 4.x nécessite Java 21 minimum
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Image finale avec Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8020
ENTRYPOINT ["java", "-jar", "app.jar"]