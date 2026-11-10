package org.example.housematesolution.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

// A real PostgreSQL in Docker for tests that need a database.
// @ServiceConnection points the app's datasource at it, so no DB_* settings are needed.
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        // Same version as Backend/docker-compose.yml and CI used
        return new PostgreSQLContainer(DockerImageName.parse("postgres:17"));
    }

}
