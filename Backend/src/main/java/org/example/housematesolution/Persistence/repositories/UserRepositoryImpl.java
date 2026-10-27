package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.example.housematesolution.Persistence.entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpa;

    public UserRepositoryImpl(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.fromDomain(user);
        UserEntity savedEntity = jpa.save(userEntity);
        return savedEntity.toDomain();
    }
    @Override
    public List<User> findByHouseId(UUID houseId){
        List<UserEntity> userEntities = jpa.findByHouseId(houseId);

        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(userEntity.toDomain());
        }
        return users;
    }
}
