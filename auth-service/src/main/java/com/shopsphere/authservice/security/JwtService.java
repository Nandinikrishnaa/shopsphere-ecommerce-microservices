package com.shopsphere.authservice.security;

import com.shopsphere.authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-ms}") long expirationMs){

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    public String generateToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime()+expirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public Claims parseToken(String token){
        return Jwts.parser().verifyWith(signingKey).build()
                .parseSignedClaims(token).getPayload();
    }

    public long getExpirationSeconds(){
        return expirationMs / 1000;
    }

}
