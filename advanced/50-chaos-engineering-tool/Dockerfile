FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/chaos-engineering-tool-1.0.0.jar chaos-agent.jar
COPY chaos-config.json .

# Create a start script to run the dummy app with the agent
RUN echo 'java -javaagent:chaos-agent.jar -cp chaos-agent.jar com.example.chaos.demo.DummyApp' > start.sh && chmod +x start.sh

CMD ["./start.sh"]
