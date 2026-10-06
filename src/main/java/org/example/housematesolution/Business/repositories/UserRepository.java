package org.example.housematesolution.Business.repositories;

import org.example.housematesolution.Business.domain.User;

public interface UserRepository {

    boolean existsByEmail(String email);

    User save(User user);
}
