# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Install dependencies to download and unzip the model
RUN apt-get update && apt-get install -y unzip wget && \
    rm -rf /var/lib/apt/lists/*

# Download and extract Vosk model (Small English model)
# URL: https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip
RUN wget -q https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip && \
    unzip vosk-model-small-en-us-0.15.zip && \
    mv vosk-model-small-en-us-0.15 model && \
    rm vosk-model-small-en-us-0.15.zip

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
