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
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY Backend/gradlew Backend/settings.gradle Backend/build.gradle ./
COPY Backend/gradle gradle
RUN ./gradlew --no-daemon dependencies

COPY Backend/src src
# Spring Boot serves everything in resources/static, so the website and the API come from the same app
COPY --from=frontend /frontend/dist src/main/resources/static
RUN ./gradlew --no-daemon bootJar
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
