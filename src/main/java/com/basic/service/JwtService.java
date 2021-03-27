package com.basic.service;

import com.basic.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${security.jwt.expiration}")
    private String expiration;

    @Value("${security.jwt.signKey}")
    private String signKey;

    public String generateToken(User user) {
        long expString = Long.valueOf(expiration);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(expString);
        Instant instant = expirationDate.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, signKey)
                .compact();

    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(signKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validToken(String token) {
        try {
            Claims claims = getClaims(token);
            Date experitionDate = claims.getExpiration();
            LocalDate date = experitionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return LocalDateTime.now().isBefore(ChronoLocalDateTime.from(date));
        }catch (Exception e) {
            return false;
        }
    }

    public String getUserLogged(String token) throws ExpiredJwtException {
        return getClaims(token).getSubject();
    }

}
