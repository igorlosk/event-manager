package dev.sorokin.eventnotificator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtValidationService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // userId хранится в кастомном поле "userId", а не в subject
        return claims.get("userId", Long.class);
    }
}
