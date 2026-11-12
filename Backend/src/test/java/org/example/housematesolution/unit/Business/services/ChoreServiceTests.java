package org.example.housematesolution.unit.Business.services;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.exceptions.ChoreAlreadyExistsException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.repositories.ChoreRepository;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.example.housematesolution.Business.services.ChoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChoreServiceTests {

    private ChoreRepository choreRepository;
    private HouseRepository houseRepository;
    private ChoreService choreService;

    private final House house = new House(UUID.randomUUID(), "Student House", "ABC123");

    @BeforeEach
    void setUp() {
        // Fake repositories, so no database is needed
        choreRepository = mock(ChoreRepository.class);
        houseRepository = mock(HouseRepository.class);
        choreService = new ChoreService(choreRepository, houseRepository, mock(UserRepository.class));
    }

    // ---------- Create a chore ----------

    @Test
    void createChoreSavesChoreInHouse() {
        when(houseRepository.findById(house.getId())).thenReturn(Optional.of(house));
        when(choreRepository.existsByHouseIdAndName(house.getId(), "dishes")).thenReturn(false);
        // Return the same chore that was passed to save()
        when(choreRepository.save(any(Chore.class))).thenAnswer(call -> call.getArgument(0));

        Chore result = choreService.createChore("dishes", 1, 2, house.getId());

        assertEquals("dishes", result.getName());
        assertEquals(1, result.getIntervalDays());
        assertEquals(2, result.getEffort());
        assertEquals(house.getId(), result.getHouse().getId());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void createChoreTrimsAndLowercasesName() {
        when(houseRepository.findById(house.getId())).thenReturn(Optional.of(house));
        when(choreRepository.existsByHouseIdAndName(house.getId(), "take out trash")).thenReturn(false);
        when(choreRepository.save(any(Chore.class))).thenAnswer(call -> call.getArgument(0));

        choreService.createChore("  Take Out Trash ", 7, 1, house.getId());

        ArgumentCaptor<Chore> savedChore = ArgumentCaptor.forClass(Chore.class);
        verify(choreRepository).save(savedChore.capture());
        assertEquals("take out trash", savedChore.getValue().getName());
    }

    @Test
    void createChoreThrowsWhenHouseDoesNotExist() {
        when(houseRepository.findById(house.getId())).thenReturn(Optional.empty());

        assertThrows(HouseNotFoundException.class,
                () -> choreService.createChore("dishes", 1, 2, house.getId()));

        verify(choreRepository, never()).save(any(Chore.class));
    }

    @Test
    void createChoreThrowsWhenChoreAlreadyExists() {
        when(houseRepository.findById(house.getId())).thenReturn(Optional.of(house));
        when(choreRepository.existsByHouseIdAndName(house.getId(), "dishes")).thenReturn(true);

        // Different case and spaces still count as the same chore
        assertThrows(ChoreAlreadyExistsException.class,
                () -> choreService.createChore(" Dishes ", 1, 2, house.getId()));

        verify(choreRepository, never()).save(any(Chore.class));
    }

    // ---------- Get chores ----------

    @Test
    void getChoresReturnsChoresOfHouse() {
        when(houseRepository.existsById(house.getId())).thenReturn(true);
        when(choreRepository.findByHouseId(house.getId())).thenReturn(
                List.of(
                        new Chore(UUID.randomUUID(), "dishes", 1, 2, LocalDateTime.now(), house),
                        new Chore(UUID.randomUUID(), "vacuum", 7, 3, LocalDateTime.now(), house)
                )
        );

        List<Chore> result = choreService.getChores(house.getId());

        assertEquals(2, result.size());
        assertEquals("dishes", result.get(0).getName());
        assertEquals("vacuum", result.get(1).getName());
    }

    @Test
    void getChoresReturnsEmptyListWhenNoChores() {
        when(houseRepository.existsById(house.getId())).thenReturn(true);
        when(choreRepository.findByHouseId(house.getId())).thenReturn(List.of());

        List<Chore> result = choreService.getChores(house.getId());

        assertEquals(0, result.size());
    }

    @Test
    void getChoresThrowsWhenHouseDoesNotExist() {
        when(houseRepository.existsById(house.getId())).thenReturn(false);

        assertThrows(HouseNotFoundException.class,
                () -> choreService.getChores(house.getId()));

        verify(choreRepository, never()).findByHouseId(any(UUID.class));
    }
}
