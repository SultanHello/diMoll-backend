# 1. Stage: Сборка JAR
FROM maven:3.9.6-eclipse-temurin-22 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src ./src

RUN mvn -q clean package -DskipTests

# 2. Stage: Запуск
FROM openjdk:22-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
