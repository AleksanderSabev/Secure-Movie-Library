package org.example.movielibraryapi.services;

import org.example.movielibraryapi.enums.Role;
import org.example.movielibraryapi.exceptions.DuplicateEntityException;
import org.example.movielibraryapi.exceptions.EntityNotFoundException;
import org.example.movielibraryapi.models.User;
import org.example.movielibraryapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createUser_shouldSaveUser_whenUsernameNotExists() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");

        User user = new User();
        user.setUsername("alice");
        user.setPassword("encodedPass");
        user.setRole(Role.USER);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = service.createUser("alice", "password123", "USER");

        assertEquals("alice", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowDuplicateEntityException_whenUsernameExists() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class,
                () -> service.createUser("alice", "password123", "USER"));

        assertTrue(ex.getMessage().contains("username"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = service.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser_shouldCallRepository_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        service.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowEntityNotFoundException_whenUserNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void updatePassword_shouldChangePassword_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = service.updatePassword(1L, "newPass");

        assertEquals("encodedNewPass", result.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_shouldThrowEntityNotFoundException_whenUserNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updatePassword(1L, "newPass"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void assignRole_shouldChangeRole_whenUserExists() {
        User user = new User();
        user.setUsername("bob");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = service.assignRole("bob", "ADMIN");

        assertEquals(Role.ADMIN, result.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void assignRole_shouldThrowEntityNotFoundException_whenUserNotExists() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.assignRole("bob", "ADMIN"));
        verify(userRepository, never()).save(any());
    }
}

