package org.example.housematesolution.Business.services;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final JoinCodeGenerator joinCodeGenerator;

    public HouseService(HouseRepository houseRepository,
                        UserRepository userRepository,
                        JoinCodeGenerator joinCodeGenerator) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
        this.joinCodeGenerator = joinCodeGenerator;
    }

    @Transactional
    public User createHouse(String houseName, String name, String email) {
        // 1. An email can only be used by one user
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        // 2. Keep generating join codes until we find one that no other house uses
        String joinCode = joinCodeGenerator.generate();
        while (houseRepository.existsByJoinCode(joinCode)) {
            joinCode = joinCodeGenerator.generate();
        }

        // 3. Save the house (id is null, the database generates it)
        House savedHouse = houseRepository.save(new House(null, houseName, joinCode));

        // 4. The person who creates the house is its first housemate
        User creator = new User(null, name, email, savedHouse);
        return userRepository.save(creator);
    }

    public User joinHouse(String joinCode, String name, String email) {
        // 1. Join codes are stored in capitals, so " abc123 " should also work
        String cleanJoinCode = joinCode.trim().toUpperCase();

        // 2. Find the house that belongs to this join code
        Optional<House> house = houseRepository.findByJoinCode(cleanJoinCode);
        if (house.isEmpty()) {
            throw new HouseNotFoundException(cleanJoinCode);
        }

        // 3. An email can only be used by one user
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        // 4. Create the new user (id is null, the database generates it) and save it
        User newUser = new User(null, name, email, house.get());
        return userRepository.save(newUser);
    }

    public List<User> getHousemates(UUID houseId) {
        if (!houseRepository.existsById(houseId)) {
            throw new HouseNotFoundException(houseId);
        }
        return userRepository.findByHouseId(houseId);
    }
}
