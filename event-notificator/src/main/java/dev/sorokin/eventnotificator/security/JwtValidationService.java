package dev.sorokin.eventnotificator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtValidationService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }
}
