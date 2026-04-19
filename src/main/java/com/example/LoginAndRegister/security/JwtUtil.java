package com.example.LoginAndRegister.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey secretKey;

    // Convert string secret menjadi SecretKey (256-bit)
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ===============================
    // GENERATE TOKEN
    // ===============================
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ===============================
    // EXTRACT USERNAME
    // ===============================
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ===============================
    // VALIDATE TOKEN
    // ===============================
    public boolean validateToken(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

    // ===============================
    // CHECK EXPIRATION
    // ===============================
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ===============================
    // EXTRACT CLAIMS
    // ===============================
    public Claims extractAllClaims(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
            throw e;

        } catch (JwtException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            throw e;
        }
    }

}
