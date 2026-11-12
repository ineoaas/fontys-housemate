package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Persistence.entities.ChoreCompletionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChoreCompletionJpaRepository extends JpaRepository<ChoreCompletionEntity, UUID> {

    List<ChoreCompletionEntity> findByChoreIdOrderByCompletedAtAsc(UUID choreId);
}
