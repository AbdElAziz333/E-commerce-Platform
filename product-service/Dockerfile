# Build Stage

FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY api-gateway/build.gradle api-gateway/
COPY product-service/build.gradle product-service/
COPY order-service/build.gradle order-service/

RUN gradle :product-service:dependencies --no-daemon

COPY product-service/src product-service/src
RUN gradle :product-service:bootJar --no-daemon

# Runtime Stage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /home/gradle/project/api-gateway/build/libs/app.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]