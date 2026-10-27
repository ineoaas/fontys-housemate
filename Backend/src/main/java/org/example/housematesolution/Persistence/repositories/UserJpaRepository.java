package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);
    List<UserEntity> findByHouseId(UUID houseId);
}