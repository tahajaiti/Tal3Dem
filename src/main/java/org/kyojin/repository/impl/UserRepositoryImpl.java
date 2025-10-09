package org.kyojin.repository.impl;

import org.kyojin.entity.User;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl extends JpaRepository<User, Long> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        return findBy("email", email);
    }
}
