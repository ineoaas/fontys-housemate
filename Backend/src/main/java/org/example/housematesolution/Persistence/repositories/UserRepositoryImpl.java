package org.example.housematesolution.Persistence.repositories;

import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.repositories.UserRepository;
import org.example.housematesolution.Persistence.entities.UserEntity;
import org.springframework.stereotype.Repository;

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
}
