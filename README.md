# Housemate Solution

A web app for people who share a house. Create a house, invite housemates with a join code, and see who lives with you.

## Tech stack

| Part | Technology |
|---|---|
| Backend | Java 21, Spring Boot 4.1, Spring Data JPA |
| Frontend | React 19, Vite 8, Oxlint |
| Database | PostgreSQL 17 |
| Hosting | Docker Compose on your own machine |

The Docker image builds the frontend and puts it inside the Spring Boot app, so the website and the API are served together on one port.

## Project structure

```
Backend/                Spring Boot app, Dockerfile, docker-compose.yml
  src/main/java/.../
    Business/           domain models, services, repository interfaces
    Persistence/        JPA entities and repository implementations
    Presentation/       REST controllers
Frontend/               React app (Vite)
.github/workflows/      CI pipeline
```

## Running the app

You need Docker. From the `Backend` folder:

```sh
docker compose up -d --build   # database + app, open http://localhost:8080
docker compose down            # stop (data is kept in the "db-data" volume)
```

## Developing

1. Start only the database: `docker compose up -d database` (in `Backend`).
2. Run the backend from IntelliJ, or with `./gradlew bootRun` (in `Backend`). It runs on port 8080.
3. Run the frontend with `npm install` and then `npm run dev` (in `Frontend`). Vite forwards API calls to the backend, so there's no CORS setup.

The database connection defaults to `localhost:5432` with user and password `housemate`. You can override it with `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME` and `DB_PASSWORD`.

## Tests

```sh
cd Backend && ./gradlew test   # needs the database running
cd Frontend && npm run lint
```

## CI

`.github/workflows/ci.yml` runs on every push and pull request. It runs the backend tests, lints and builds the frontend, and checks that the Docker image builds. The `ci-ok` job only passes when all of them pass. On `main`, SonarQube Cloud also analyzes the code against a Quality Gate (see `sonar-project.properties`).
