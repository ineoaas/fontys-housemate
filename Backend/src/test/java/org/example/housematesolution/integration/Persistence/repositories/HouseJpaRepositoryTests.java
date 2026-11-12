package org.example.housematesolution.integration.Persistence.repositories;

import org.example.housematesolution.Persistence.entities.HouseEntity;
import org.example.housematesolution.Persistence.repositories.HouseJpaRepository;
import org.example.housematesolution.integration.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// Runs the repository against a real PostgreSQL (see TestcontainersConfiguration).
// Each test runs in a transaction that is rolled back afterwards.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class HouseJpaRepositoryTests {

    @Autowired
    private HouseJpaRepository houseJpaRepository;

    @Test
    void findByJoinCodeReturnsTheHouseWithThatCode() {
        HouseEntity saved = houseJpaRepository.saveAndFlush(new HouseEntity(null, "Main Street", "ABC123"));
        houseJpaRepository.saveAndFlush(new HouseEntity(null, "Other House", "XYZ789"));

        Optional<HouseEntity> found = houseJpaRepository.findByJoinCode("ABC123");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getName()).isEqualTo("Main Street");
    }

    @Test
    void findByJoinCodeReturnsEmptyWhenNoHouseHasThatCode() {
        houseJpaRepository.saveAndFlush(new HouseEntity(null, "Main Street", "ABC123"));

        assertThat(houseJpaRepository.findByJoinCode("NOPE00")).isEmpty();
    }

    @Test
    void findByJoinCodeIsCaseSensitive() {
        houseJpaRepository.saveAndFlush(new HouseEntity(null, "Main Street", "ABC123"));

        assertThat(houseJpaRepository.findByJoinCode("abc123")).isEmpty();
    }

    @Test
    void existsByJoinCodeIsTrueWhenAHouseHasThatCode() {
        houseJpaRepository.saveAndFlush(new HouseEntity(null, "Main Street", "ABC123"));

        assertThat(houseJpaRepository.existsByJoinCode("ABC123")).isTrue();
    }

    @Test
    void existsByJoinCodeIsFalseWhenNoHouseHasThatCode() {
        houseJpaRepository.saveAndFlush(new HouseEntity(null, "Main Street", "ABC123"));

        assertThat(houseJpaRepository.existsByJoinCode("NOPE00")).isFalse();
    }

}
