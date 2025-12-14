package org.example.movielibraryapi.repositories;

import lombok.NonNull;
import org.example.movielibraryapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}