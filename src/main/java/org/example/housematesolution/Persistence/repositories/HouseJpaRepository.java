package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Persistence.entities.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HouseJpaRepository extends JpaRepository<HouseEntity, UUID> {

    Optional<HouseEntity> findByJoinCode(String joinCode);
}
