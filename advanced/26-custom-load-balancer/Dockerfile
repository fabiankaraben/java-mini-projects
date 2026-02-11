FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/distributions/custom-load-balancer-1.0-SNAPSHOT.tar /app/
RUN tar -xvf custom-load-balancer-1.0-SNAPSHOT.tar && \
    rm custom-load-balancer-1.0-SNAPSHOT.tar
WORKDIR /app/custom-load-balancer-1.0-SNAPSHOT
ENTRYPOINT ["bin/custom-load-balancer"]
