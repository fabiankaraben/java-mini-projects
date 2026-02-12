# Using a multi-stage build for efficiency
FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . .
# Skip tests during build to speed up and avoid needing Redis at build time
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
