
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the application jar
COPY --from=build /app/application/target/application-*.jar app.jar
# Create plugins directory
RUN mkdir plugins
# Copy the welcome-plugin jar to plugins directory
COPY --from=build /app/welcome-plugin/target/welcome-plugin-*.jar plugins/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
