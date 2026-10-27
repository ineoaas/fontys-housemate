package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.repositories.HouseRepository;
import org.example.housematesolution.Persistence.entities.HouseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public boolean existsByJoinCode(String joinCode) {
        return jpa.existsByJoinCode(joinCode);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    public House save(House house) {
        HouseEntity houseEntity = HouseEntity.fromDomain(house);
        HouseEntity savedEntity = jpa.save(houseEntity);
        return savedEntity.toDomain();
    }
}
