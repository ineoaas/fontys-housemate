package org.example.housematesolution.Business.services;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public HouseService(HouseRepository houseRepository, UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    public User joinHouse(String joinCode, String name, String email) {
        // 1. Find the house that belongs to this join code
        Optional<House> house = houseRepository.findByJoinCode(joinCode);
        if (house.isEmpty()) {
            throw new HouseNotFoundException(joinCode);
        }

        // 2. An email can only be used by one user
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        // 3. Create the new user (id is null, the database generates it) and save it
        User newUser = new User(null, name, email, house.get());
        return userRepository.save(newUser);
    }
}
