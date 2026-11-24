package com.hms.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private long expiryTime;

    private Algorithm algorithm;

    @PostConstruct
    public void initialize() {
        if (algorithmKey == null || algorithmKey.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must contain at least 32 characters");
        }
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalStateException("JWT issuer must not be blank");
        }
        if (expiryTime <= 0) {
            throw new IllegalStateException("JWT expiry duration must be positive");
        }
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }

        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(username)
                .withClaim("name", username)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);

        String username = decodedJWT.getSubject();
        return username != null ? username : decodedJWT.getClaim("name").asString();
    }
}
