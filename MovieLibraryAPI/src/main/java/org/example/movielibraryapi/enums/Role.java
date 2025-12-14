package org.example.movielibraryapi.enums;

public enum Role {
    ADMIN,
    USER;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase()); // ignore case
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}