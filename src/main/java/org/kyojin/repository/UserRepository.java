package org.kyojin.repository;

import org.kyojin.entity.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByEmail(String email);
}
