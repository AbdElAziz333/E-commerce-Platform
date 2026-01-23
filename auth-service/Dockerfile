# Build Stage

FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY auth-service/build.gradle auth-service/

RUN gradle :auth-service:dependencies --no-daemon

COPY auth-service/src auth-service/src
RUN gradle :auth-service:bootJar --no-daemon

# Runtime Stage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /home/gradle/project/auth-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]