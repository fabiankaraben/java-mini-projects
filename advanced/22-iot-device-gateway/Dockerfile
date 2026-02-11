FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src src

RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
