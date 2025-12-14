package org.example.movielibraryapi.models.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private static final String USERNAME_REQUIRED = "Username is required";
    private static final String PASSWORD_REQUIRED = "Password is required";

    @NotBlank(message = USERNAME_REQUIRED)
    private String username;

    @NotBlank(message = PASSWORD_REQUIRED)
    private String password;

    private String role;
}