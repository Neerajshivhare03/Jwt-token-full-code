package com.hms.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        ReflectionTestUtils.setField(
                jwtService,
                "algorithmKey",
                "test-only-secret-key-with-at-least-32-characters"
        );
        ReflectionTestUtils.setField(jwtService, "issuer", "hms-test");
        ReflectionTestUtils.setField(jwtService, "expiryTime", 60_000L);
        jwtService.initialize();
    }

    @Test
    void shouldGenerateAndVerifyToken() {
        String token = jwtService.generateToken("neeraj");

        assertNotNull(token);
        assertEquals("neeraj", jwtService.getUsername(token));
    }

    @Test
    void shouldRejectTokenWithInvalidSignature() {
        String token = jwtService.generateToken("neeraj");
        String tamperedToken = token.substring(0, token.length() - 1) + "x";

        assertThrows(
                JWTVerificationException.class,
                () -> jwtService.getUsername(tamperedToken)
        );
    }

    @Test
    void shouldRejectBlankUsername() {
        assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.generateToken(" ")
        );
    }
}
