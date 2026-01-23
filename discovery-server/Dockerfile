# Build Stage

FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY discovery-server/build.gradle discovery-server/

RUN gradle :discovery-server:dependencies --no-daemon

COPY discovery-server/src discovery-server/src
RUN gradle :discovery-server:bootJar --no-daemon

# Runtime Stage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /home/gradle/project/discovery-server/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]