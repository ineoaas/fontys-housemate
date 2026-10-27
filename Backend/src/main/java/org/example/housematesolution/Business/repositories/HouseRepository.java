package org.example.housematesolution.Business.repositories;

import org.example.housematesolution.Business.domain.House;

import java.util.Optional;
import java.util.UUID;

public interface HouseRepository {

    Optional<House> findByJoinCode(String joinCode);

    boolean existsByJoinCode(String joinCode);
    boolean existsById(UUID id);

    House save(House house);
}
