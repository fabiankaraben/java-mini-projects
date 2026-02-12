FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
