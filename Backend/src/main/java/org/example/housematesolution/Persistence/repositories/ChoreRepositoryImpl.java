package org.example.housematesolution.Persistence.repositories;


import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.repositories.ChoreRepository;
import org.example.housematesolution.Persistence.entities.ChoreEntity;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChoreRepositoryImpl implements ChoreRepository {

    private final ChoreJpaRepository jpa;

    public ChoreRepositoryImpl(ChoreJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Chore> findById(UUID id) {
        Optional<ChoreEntity> choreEntity = jpa.findById(id);

        if (choreEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(choreEntity.get().toDomain());
    }

    @Override
    public List<Chore> findByHouseId(UUID houseId) {
        List<ChoreEntity> choreEntities = jpa.findByHouseId(houseId);

        List<Chore> chore = new ArrayList<>();
        for (ChoreEntity choreEntity : choreEntities) {
            chore.add(choreEntity.toDomain());
        }
        return chore;
    }

    @Override
    public Chore save(Chore chore) {
        ChoreEntity choreEntity = ChoreEntity.fromDomain(chore);
        ChoreEntity savedEntity = jpa.save(choreEntity);
        return savedEntity.toDomain();
    }

    @Override
    public List<ChoreCompletion> findCompletionsByChoreId(UUID choreId) {
        return List.of();
    }

    @Override
    public ChoreCompletion saveCompletion(ChoreCompletion choreCompletion) {
        return null;
    }

    @Override
    public boolean existsByHouseIdAndName(UUID houseId, String name) {
        return jpa.existsByHouseIdAndName(houseId, name);
    }
}
