package org.example.housematesolution.integration.Persistence.repositories;

import jakarta.persistence.EntityManager;
import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Persistence.entities.ChoreEntity;
import org.example.housematesolution.Persistence.entities.HouseEntity;
import org.example.housematesolution.Persistence.entities.UserEntity;
import org.example.housematesolution.Persistence.repositories.ChoreJpaRepository;
import org.example.housematesolution.Persistence.repositories.ChoreRepositoryImpl;
import org.example.housematesolution.Persistence.repositories.HouseJpaRepository;
import org.example.housematesolution.Persistence.repositories.UserJpaRepository;
import org.example.housematesolution.integration.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Runs ChoreRepositoryImpl against a real PostgreSQL (see TestcontainersConfiguration).
// Each test runs in a transaction that is rolled back afterwards.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainersConfiguration.class, ChoreRepositoryImpl.class})
class ChoreRepositoryImplTests {

    // Fixed times, because Postgres stores microseconds and LocalDateTime.now() can have nanoseconds
    private static final LocalDateTime MONDAY = LocalDateTime.of(2026, 11, 2, 9, 0);
    private static final LocalDateTime TUESDAY = MONDAY.plusDays(1);
    private static final LocalDateTime WEDNESDAY = MONDAY.plusDays(2);

    @Autowired
    private ChoreRepositoryImpl choreRepository;

    @Autowired
    private HouseJpaRepository houseJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ChoreJpaRepository choreJpaRepository;

    @Autowired
    private EntityManager entityManager;

    private House house;
    private User alice;
    private User bob;
    private Chore dishes;
    private Chore trash;

    @BeforeEach
    void setUp() {
        HouseEntity houseEntity = houseJpaRepository.save(new HouseEntity(null, "Main Street", "ABC123"));
        UserEntity aliceEntity = userJpaRepository.save(new UserEntity(null, "Alice", "alice@example.com", houseEntity));
        UserEntity bobEntity = userJpaRepository.save(new UserEntity(null, "Bob", "bob@example.com", houseEntity));
        ChoreEntity dishesEntity = choreJpaRepository.save(new ChoreEntity(null, "Dishes", 1, 1, MONDAY, houseEntity));
        ChoreEntity trashEntity = choreJpaRepository.save(new ChoreEntity(null, "Trash", 7, 2, MONDAY, houseEntity));

        house = houseEntity.toDomain();
        alice = aliceEntity.toDomain();
        bob = bobEntity.toDomain();
        dishes = dishesEntity.toDomain();
        trash = trashEntity.toDomain();
    }

    // Writes everything to the database and forgets the cached objects, so the next read really hits Postgres
    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void saveCompletionStoresTheCompletionAndReturnsItWithAnId() {
        ChoreCompletion saved = choreRepository.saveCompletion(
                new ChoreCompletion(null, MONDAY, dishes, house, alice, bob));
        flushAndClear();

        assertThat(saved.getId()).isNotNull();

        List<ChoreCompletion> stored = choreRepository.findCompletionsByChoreId(dishes.getId());
        assertThat(stored).hasSize(1);
        ChoreCompletion completion = stored.getFirst();
        assertThat(completion.getId()).isEqualTo(saved.getId());
        assertThat(completion.getCompletedAt()).isEqualTo(MONDAY);
        assertThat(completion.getChore().getId()).isEqualTo(dishes.getId());
        assertThat(completion.getHouse().getId()).isEqualTo(house.getId());
        assertThat(completion.getAssignedTo().getId()).isEqualTo(alice.getId());
        assertThat(completion.getDoneBy().getId()).isEqualTo(bob.getId());
    }

    @Test
    void saveCompletionWorksWithoutAnAssignee() {
        choreRepository.saveCompletion(new ChoreCompletion(null, MONDAY, dishes, house, null, alice));
        flushAndClear();

        List<ChoreCompletion> stored = choreRepository.findCompletionsByChoreId(dishes.getId());
        assertThat(stored).hasSize(1);
        assertThat(stored.getFirst().getAssignedTo()).isNull();
        assertThat(stored.getFirst().getDoneBy().getId()).isEqualTo(alice.getId());
    }

    @Test
    void findCompletionsByChoreIdReturnsOldestFirst() {
        // Saved out of order on purpose
        choreRepository.saveCompletion(new ChoreCompletion(null, WEDNESDAY, dishes, house, alice, alice));
        choreRepository.saveCompletion(new ChoreCompletion(null, MONDAY, dishes, house, alice, alice));
        choreRepository.saveCompletion(new ChoreCompletion(null, TUESDAY, dishes, house, alice, bob));
        flushAndClear();

        List<ChoreCompletion> stored = choreRepository.findCompletionsByChoreId(dishes.getId());

        assertThat(stored)
                .extracting(ChoreCompletion::getCompletedAt)
                .containsExactly(MONDAY, TUESDAY, WEDNESDAY);
    }

    @Test
    void findCompletionsByChoreIdOnlyReturnsThatChoresCompletions() {
        choreRepository.saveCompletion(new ChoreCompletion(null, MONDAY, dishes, house, alice, alice));
        choreRepository.saveCompletion(new ChoreCompletion(null, TUESDAY, trash, house, bob, bob));
        flushAndClear();

        List<ChoreCompletion> stored = choreRepository.findCompletionsByChoreId(trash.getId());

        assertThat(stored).hasSize(1);
        assertThat(stored.getFirst().getChore().getId()).isEqualTo(trash.getId());
    }

    @Test
    void findCompletionsByChoreIdReturnsEmptyWhenTheChoreWasNeverDone() {
        assertThat(choreRepository.findCompletionsByChoreId(dishes.getId())).isEmpty();
    }

}
