package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Persistence.entities.ChoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChoreJpaRepository extends JpaRepository<ChoreEntity, UUID> {

    List<ChoreEntity> findByHouseId(UUID house_id);

    boolean existsByHouseIdAndName(UUID houseId, String name);
}
