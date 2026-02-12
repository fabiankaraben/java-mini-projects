FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
# Skip tests during build as they require Docker socket which might not be available in build phase
# depending on how the build is run. We'll run tests separately or ensure socket is mounted.
# For the 'final' image, we just need the jar.
RUN gradle clean bootJar -x test --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# Install Docker CLI if needed for debugging, but the app uses Docker Java API via socket
# ensuring the socket is mounted is key.
ENTRYPOINT ["java", "-jar", "app.jar"]
