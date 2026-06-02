package dev.sorokin.eventnotificator.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenManager {

    private final SecretKey secretKey;

    public JwtTokenManager(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userId", Long.class);
    }

    public String getLoginFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getUserRole(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userRole", String.class);
    }
}
