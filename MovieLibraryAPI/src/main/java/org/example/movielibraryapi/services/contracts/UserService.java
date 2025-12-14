package org.example.movielibraryapi.services.contracts;

import org.example.movielibraryapi.models.User;

import java.util.List;

public interface UserService {

    User createUser(String username, String rawPassword, String role);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User updatePassword(Long id, String newPassword);

    User assignRole(String username, String role);
}

