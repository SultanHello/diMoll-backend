package org.example.dimollbackend.auth.service.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.config.properties.JwtProperties;
import org.example.dimollbackend.enums.TokenType;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private final String SECRET_KEY ="4b4d109b8a545947e3d06e7856aad9a97f546e19330280128170df02bbe3e211";

    public SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }
    public Claims extractAllClaims(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token.trim())
                .getBody();
    }
    public <T>T extractClaim(String token, Function<Claims,T> reselver){
        Claims claims =extractAllClaims(token);
        return  reselver.apply(claims);
    }


    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }


    public String generateToken(String email, TokenType tokenType) {
        long expirationMillis = switch (tokenType) {
            case ACCESS_TOKEN -> jwtProperties.getAccessTokenTtl();
            case REFRESH_TOKEN -> jwtProperties.getRefreshTokenTtl();
        };

        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis+ 1000 * 60 * 60))
                .signWith(getSignKey())
                .compact();
    }
    public boolean isTokenValid(String jwtToken) {
        try {
            return extractExpiration(jwtToken).after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
