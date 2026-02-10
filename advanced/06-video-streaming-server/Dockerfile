FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jdk-jammy

# Install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the build artifact from builder stage
COPY --from=builder /app/target/libs/video-streaming-server-0.0.1-SNAPSHOT.jar app.jar

# Create directory for HLS segments
RUN mkdir -p /app/hls

# Expose ports: 8080 (Web/HLS), 1935 (RTMP input handled by FFmpeg)
EXPOSE 8080 1935

ENTRYPOINT ["java", "-jar", "app.jar"]
