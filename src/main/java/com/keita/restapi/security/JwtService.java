package com.keita.restapi.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.keita.restapi.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${EXPIRATION_TIME}")
    private long expiration_time;

    @Value("${SECRET_KEY}")
    private String secret_key;

    /**
     * Generate a JWT (JSON Web Token) for the specified user and claims.
     *
     * @param user The user for whom the token is generated.
     * @param claims additional claims to add it in the token
     * @return The generated JWT.
    */


    public String generateToken(User user, Map<String, Object> claims) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (expiration_time * 60 * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(generateKeyToken(), SignatureAlgorithm.HS256)
                .compact();


    }

    /**
     * Decode the secret key from base64 and create cryptographic key token
     *
     * @return The cryptographic key token.
     */

    private Key generateKeyToken() {
        // Decode the secret key from Base64 and create a key token
        byte[] secretKeyAsBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(secretKeyAsBytes);
    }

    /**
     * Extract the username from the specified JWT.
     *
     * @param jwt The JWT from which to extract the username.
     * @return The extracted username.
     */

    public String extractUsername(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    /**
     * Parse and extract claims from the specified JWT.
     *
     * @param jwt The JWT from which to extract claims.
     * @return The extracted claims.
     */

    private Claims extractClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(generateKeyToken()).build()
                .parseClaimsJws(jwt).getBody();
    }
}
