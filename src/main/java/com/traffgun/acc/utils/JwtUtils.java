package com.traffgun.acc.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    @Getter
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    @Getter
    private long refreshExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(String username, boolean totpVerified) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        claims.put("totpVerified", totpVerified);
        return createToken(claims, username, accessExpiration);
    }

    // ---- REFRESH TOKEN ----
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("totpVerified", true); // refresh tokens always assume verified
        return createToken(claims, username, refreshExpiration);
    }

    // ---- GENERIC TOKEN CREATION ----
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ---- PARSING / VALIDATION ----
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(token)
                    && expectedType.equals(claims.get("type", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isTotpVerified(String token) {
        Object claim = extractAllClaims(token).get("totpVerified");
        return claim != null && (Boolean) claim;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }
}