package org.example.housematesolution.Business.repositories;

import org.example.housematesolution.Business.domain.House;

import java.util.Optional;

public interface HouseRepository {

    Optional<House> findByJoinCode(String joinCode);
}
