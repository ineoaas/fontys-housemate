package org.example.housematesolution.Business.repositories;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChoreRepository {

    Optional<Chore> findById(UUID id);
    List<Chore> findByHouseId(UUID houseId);
    Chore save(Chore chore);

    // Oldest first
    List<ChoreCompletion> findCompletionsByChoreId(UUID choreId);
    ChoreCompletion saveCompletion(ChoreCompletion choreCompletion);

    boolean existsByHouseIdAndName(UUID houseId, String name);
}
