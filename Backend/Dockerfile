# Stage 1: build the jar (needs the full JDK)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew --no-daemon dependencies

COPY src src
RUN ./gradlew --no-daemon bootJar
RUN mv build/libs/*.jar app.jar

# Stage 2: run the jar (only needs the smaller JRE, and nothing from the build is left behind)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/app.jar app.jar

# Don't run the app as root
RUN useradd --system spring
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
