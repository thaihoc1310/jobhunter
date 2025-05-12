# Stage 1: Build the application
FROM gradle:8.7-jdk21-alpine AS build
WORKDIR /jobhunter
COPY --chown=gradle:gradle . .

#skip task: test
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /run
COPY --from=build /jobhunter/build/libs/*.jar /run/spring-boot-job-hunter.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/run/spring-boot-job-hunter.jar"]
