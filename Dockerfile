# Build Stage
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew build -x test

# Run Stage
FROM openjdk:21-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=UTC", "-jar", "app.jar"]