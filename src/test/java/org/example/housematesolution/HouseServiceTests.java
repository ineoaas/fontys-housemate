package org.example.housematesolution;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.example.housematesolution.Business.services.HouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private HouseService houseService;

    private final House house = new House(UUID.randomUUID(), "Student House", "ABC123");

    @BeforeEach
    void setUp() {
        // Fake repositories, so no database is needed
        houseRepository = mock(HouseRepository.class);
        userRepository = mock(UserRepository.class);
        houseService = new HouseService(houseRepository, userRepository);
    }

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
