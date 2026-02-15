# 1. Build stage (Maven)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем pom.xml и зависимости
COPY pom.xml .

#скачиваем зависимости
RUN mvn dependency:go-offline

# Копируем исходники и собираем jar
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Runtime stage (JRE)
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]