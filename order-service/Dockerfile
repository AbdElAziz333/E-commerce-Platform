# Build Stage

FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY order-service/build.gradle order-service/

RUN gradle :order-service:dependencies --no-daemon

COPY order-service/src order-service/src
RUN gradle :order-service:bootJar --no-daemon

# Runtime Stage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /home/gradle/project/order-service/build/libs/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]