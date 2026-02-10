FROM eclipse-temurin:17-jdk-jammy

# Install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

# Grant execution rights to the wrapper
RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/video-thumbnail-generator-0.0.1-SNAPSHOT.jar"]
