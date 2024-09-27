package com.hrelix.app.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final String secretKey = "d5c8f48ef1681555faa189715e863d4faf60c332fca29f83c5761d4da1134278"; // A strong secret key

    // Extracts the subject (username/userId) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extracts all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())  // Use parserBuilder and setSigningKey
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Checks if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generates a new JWT token with claims and userId as the subject
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }

    // Creates the JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .setHeaderParam("typ", "JWT")  // Sets the token type to JWT
                .signWith(getSecretKey())  // Signs the token with the secret key
                .compact();
    }

    // Validates the token by checking the subject and expiration
    public Boolean validateToken(String token, String userId) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userId) && !isTokenExpired(token));
    }

    // Converts the secret key into a proper SecretKey for signing the JWT
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}