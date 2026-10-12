package org.example.housematesolution;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.example.housematesolution.Business.services.HouseService;
import org.example.housematesolution.Business.services.JoinCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HouseServiceTests {

    private HouseRepository houseRepository;
    private UserRepository userRepository;
    private JoinCodeGenerator joinCodeGenerator;
    private HouseService houseService;

    private final House house = new House(UUID.randomUUID(), "Student House", "ABC123");

    @BeforeEach
    void setUp() {
        // Fake repositories and generator, so no database is needed
        houseRepository = mock(HouseRepository.class);
        userRepository = mock(UserRepository.class);
        joinCodeGenerator = mock(JoinCodeGenerator.class);
        houseService = new HouseService(houseRepository, userRepository, joinCodeGenerator);
    }

    // ---------- Create a house ----------

    @Test
    void createHouseSavesHouseAndCreator() {
        when(userRepository.existsByEmail("inci@example.com")).thenReturn(false);
        when(joinCodeGenerator.generate()).thenReturn("ABC123");
        when(houseRepository.existsByJoinCode("ABC123")).thenReturn(false);
        when(houseRepository.save(any(House.class))).thenReturn(house);
        // Return the same user that was passed to save()
        when(userRepository.save(any(User.class))).thenAnswer(call -> call.getArgument(0));

        User result = houseService.createHouse("Student House", "Inci", "inci@example.com");

        // The house was saved with the generated join code
        ArgumentCaptor<House> savedHouse = ArgumentCaptor.forClass(House.class);
        verify(houseRepository).save(savedHouse.capture());
        assertEquals("Student House", savedHouse.getValue().getName());
        assertEquals("ABC123", savedHouse.getValue().getJoinCode());

        // The creator lives in the new house
        assertEquals("Inci", result.getName());
        assertEquals(house.getId(), result.getHouse().getId());
    }

    @Test
    void createHouseThrowsWhenEmailIsAlreadyUsed() {
        when(userRepository.existsByEmail("inci@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class,
                () -> houseService.createHouse("Student House", "Inci", "inci@example.com"));

        verify(houseRepository, never()).save(any(House.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createHouseGeneratesNewCodeWhenCodeIsTaken() {
        when(userRepository.existsByEmail("inci@example.com")).thenReturn(false);
        // First code is already used by another house, second one is free
        when(joinCodeGenerator.generate()).thenReturn("TAKEN1", "FREE22");
        when(houseRepository.existsByJoinCode("TAKEN1")).thenReturn(true);
        when(houseRepository.existsByJoinCode("FREE22")).thenReturn(false);
        when(houseRepository.save(any(House.class))).thenReturn(house);
        when(userRepository.save(any(User.class))).thenAnswer(call -> call.getArgument(0));

        houseService.createHouse("Student House", "Inci", "inci@example.com");

        ArgumentCaptor<House> savedHouse = ArgumentCaptor.forClass(House.class);
        verify(houseRepository).save(savedHouse.capture());
        assertEquals("FREE22", savedHouse.getValue().getJoinCode());
    }

    // ---------- Join a house ----------

    @Test
    void joinHouseSavesUserWhenJoinCodeIsValid() {
        UUID userId = UUID.randomUUID();
        User savedUser = new User(userId, "Inci", "inci@example.com", house);

        when(houseRepository.findByJoinCode("ABC123")).thenReturn(Optional.of(house));
        when(userRepository.existsByEmail("inci@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = houseService.joinHouse("ABC123", "Inci", "inci@example.com");

        assertEquals(userId, result.getId());
        assertEquals("Inci", result.getName());
        assertEquals(house.getId(), result.getHouse().getId());
    }

    @Test
    void joinHouseIgnoresCaseAndSpacesInJoinCode() {
        when(houseRepository.findByJoinCode("ABC123")).thenReturn(Optional.of(house));
        when(userRepository.existsByEmail("inci@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(call -> call.getArgument(0));

        User result = houseService.joinHouse("  abc123 ", "Inci", "inci@example.com");

        assertEquals(house.getId(), result.getHouse().getId());
    }

    @Test
    void joinHouseThrowsWhenJoinCodeIsWrong() {
        when(houseRepository.findByJoinCode("WRONG")).thenReturn(Optional.empty());

        assertThrows(HouseNotFoundException.class,
                () -> houseService.joinHouse("WRONG", "Inci", "inci@example.com"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void joinHouseThrowsWhenEmailIsAlreadyUsed() {
        when(houseRepository.findByJoinCode("ABC123")).thenReturn(Optional.of(house));
        when(userRepository.existsByEmail("inci@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class,
                () -> houseService.joinHouse("ABC123", "Inci", "inci@example.com"));

        verify(userRepository, never()).save(any(User.class));
    }
}
