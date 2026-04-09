package com.example.movie.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.movie.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailIgnoreCase(String email);

    User findByResetToken(String resetToken);
}