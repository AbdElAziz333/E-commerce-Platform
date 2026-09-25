# Build Stage

FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project

ARG SVC_NAME

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY api-gateway/build.gradle api-gateway/
COPY product-service/build.gradle product-service/
COPY order-service/build.gradle order-service/

RUN gradle :${SVC_NAME}:dependencies --no-daemon

COPY api-gateway/src api-gateway/src
COPY product-service/src product-service/src
COPY order-service/src order-service/src

RUN gradle :${SVC_NAME}:bootJar --no-daemon

RUN cp /home/gradle/project/${SVC_NAME}/build/libs/app.jar /app.jar

# Runtime Stage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app.jar app.jar

EXPOSE ${SVC_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]