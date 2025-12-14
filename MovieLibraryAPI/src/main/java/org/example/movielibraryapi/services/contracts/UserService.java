package org.example.movielibraryapi.services.contracts;

import org.example.movielibraryapi.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(String username, String rawPassword, String role);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User updatePassword(Long id, String newPassword);
}

