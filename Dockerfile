# Spring Boot 3.x — Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Image finale
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8020

# Passe les variables Railway directement à Spring Boot au démarrage
ENTRYPOINT ["sh", "-c", "java -jar app.jar \
  --spring.datasource.url=${SPRING_DATASOURCE_URL} \
  --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
  --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
  --server.port=${PORT:-8020}"]