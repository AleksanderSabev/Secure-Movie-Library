package org.example.movielibraryapi.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.movielibraryapi.models.User;
import org.example.movielibraryapi.models.dtos.auth.LoginDto;
import org.example.movielibraryapi.models.dtos.auth.RegisterDto;
import org.example.movielibraryapi.services.auth.JwtService;
import org.example.movielibraryapi.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String LOGOUT_MESSAGE = "Logged out successfully";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return jwtService.generateToken(request.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<@NonNull Void> register(@Valid @RequestBody RegisterDto request) {
        try{
            User user = userService.createUser(
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole()
            );
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", LOGOUT_MESSAGE);
        return ResponseEntity.ok(response);
    }
}

