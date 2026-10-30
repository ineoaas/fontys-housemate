package org.example.housematesolution.Business.repositories;

import org.example.housematesolution.Business.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    boolean existsByEmail(String email);
    List<User> findByHouseId(UUID houseId);

    User save(User user);
}
