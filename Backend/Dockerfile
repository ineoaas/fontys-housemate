# Build from the repository root, because the image needs both Frontend and Backend:
#   docker build -f Backend/Dockerfile .

# Stage 1: build the React frontend into static files
FROM node:24-alpine AS frontend
WORKDIR /frontend
COPY Frontend/package.json Frontend/package-lock.json ./
RUN npm ci
COPY Frontend ./
RUN npm run build

# Stage 2: build the jar (needs the full JDK)
# The Gradle image already has Gradle installed, so the build doesn't depend on downloading it
# from services.gradle.org (which timed out on GitHub). Keep the version the same as in
# gradle/wrapper/gradle-wrapper.properties.
FROM gradle:9.7.1-jdk21 AS build
WORKDIR /app
COPY Backend/settings.gradle Backend/build.gradle ./
RUN gradle --no-daemon dependencies

COPY Backend/src src
# Spring Boot serves everything in resources/static, so the website and the API come from the same app
COPY --from=frontend /frontend/dist src/main/resources/static
RUN gradle --no-daemon bootJar
RUN mv build/libs/*.jar app.jar

# Stage 3: run the jar (only needs the smaller JRE, and nothing from the build is left behind)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/app.jar app.jar

# Don't run the app as root
RUN useradd --system spring
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
