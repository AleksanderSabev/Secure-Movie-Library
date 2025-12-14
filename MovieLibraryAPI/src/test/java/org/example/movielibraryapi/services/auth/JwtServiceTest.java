package org.example.movielibraryapi.services.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;


    @Test
    void generateToken_shouldContainUsername() {
        String username = "alice";
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        String extracted = jwtService.extractUsername(token);
        assertEquals(username, extracted);
    }

    @Test
    void extractExpiration_shouldReturnValidDate() {
        String token = jwtService.generateToken("alice");
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void isTokenValid_shouldReturnTrue_forValidToken() {
        String username = "alice";
        String token = jwtService.generateToken(username);

        when(userDetails.getUsername()).thenReturn(username);

        boolean valid = jwtService.isTokenValid(token, userDetails);
        assertTrue(valid);
    }

    @Test
    void isTokenValid_shouldReturnFalse_forInvalidUsername() {
        String token = jwtService.generateToken("alice");

        when(userDetails.getUsername()).thenReturn("bob");

        boolean valid = jwtService.isTokenValid(token, userDetails);
        assertFalse(valid);
    }

    @Test
    void isTokenValid_shouldReturnFalse_forExpiredToken() throws InterruptedException {
        // Create a token with very short expiration
        JwtService shortLivedService = new JwtService() {
            @Override
            public String generateToken(String username) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 10)) // 10ms
                        .signWith(getKey())
                        .compact();
            }
        };

        String token = shortLivedService.generateToken("alice");
        Thread.sleep(50); // wait until token expires

        lenient().when(userDetails.getUsername()).thenReturn("alice");

        assertFalse(shortLivedService.isTokenValid(token, userDetails));
    }

    @Test
    void extractUsername_shouldThrowException_forInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }
}

