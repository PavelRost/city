package org.rostfactory.auth.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.rostfactory.auth.entity.User;
import org.rostfactory.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtServiceImpl implements JwtService {

    private final SecretKey jwtAccessSecret;

    public JwtServiceImpl(@Value("${jwt-secret-key}") String jwtAccessSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .signWith(jwtAccessSecret)
                .claim("role", user.getRole())
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
