package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Persistence.entities.HouseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HouseRepositoryImpl implements HouseRepository {

    private final HouseJpaRepository jpa;

    public HouseRepositoryImpl(HouseJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<House> findByJoinCode(String joinCode) {
        Optional<HouseEntity> houseEntity = jpa.findByJoinCode(joinCode);

        if (houseEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(houseEntity.get().toDomain());
    }
}
