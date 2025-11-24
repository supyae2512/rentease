package com.rentease.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JWT {

	private final String secretKey = "nihon2025_super_secure_jwt_key_32bytes_min";

    private final long expirationMs = 1000 * 60 * 120; // 2 HOURS
    
    private SecretKey getSigningKey() {
        // JJWT requires a Key object when using 0.11.x+
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) { return false; }
//    }
//    
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
    

//    public Long extractUserId(String token) {
//        return Jwts.parserBuilder().setSigningKey(secretKey)
//                .build().parseClaimsJws(token)
//                .getBody().get("userId", Long.class);
//    }

    public Long extractUserId(String token) {
        Claims claims = validateToken(token);
        if (claims == null) return null;
        return claims.get("userId", Long.class);
    }
    
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


}
