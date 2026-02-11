FROM gradle:8.6.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean installDist --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/install/custom-protocol-server /app
EXPOSE 8080
CMD ["./bin/custom-protocol-server", "8080"]
