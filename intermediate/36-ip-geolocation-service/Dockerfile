FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# Create directory for the database
RUN mkdir -p src/main/resources
ENTRYPOINT ["java", "-jar", "app.jar"]
