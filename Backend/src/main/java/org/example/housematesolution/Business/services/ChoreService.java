package org.example.housematesolution.Business.services;

import jakarta.transaction.Transactional;
import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.exceptions.ChoreAlreadyExistsException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.repositories.ChoreRepository;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public ChoreService(ChoreRepository choreRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.choreRepository = choreRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Chore createChore(String choreName, int intervalDays, int effort, UUID houseId ) {

        String cleanedName = choreName.trim().toLowerCase();
        // 1. choreName cannot have duplicates
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));

        if (choreRepository.existsByHouseIdAndName(houseId, cleanedName)) {
            throw new ChoreAlreadyExistsException(cleanedName);
        }

        Chore chore = new Chore(null, cleanedName, intervalDays, effort, LocalDateTime.now(), house);
        return choreRepository.save(chore);
    }
}
