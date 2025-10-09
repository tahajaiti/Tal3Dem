package org.kyojin.repository.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.User;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.UserRepository;

import java.util.Optional;

@Implementation(UserRepository.class)
@Injectable
public class UserRepositoryImpl extends JpaRepository<User, Long> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        return findBy("email", email);
    }
}
