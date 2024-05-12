package com.todimu.backend.dropboxclone.security.jwt;

import com.todimu.backend.dropboxclone.exception.TokenValidationFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenProvider {

    @Value(value = "${jwt.key}")
    private String jwtKey;

    public Token createToken(Authentication authentication) {
        Token token = null;
        if (authentication != null) {
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
            String jwtToken = Jwts.builder().setIssuer("todimu").setSubject("access-token")
                    .claim("userId", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(setExpirationTime())
                    .signWith(secretKey)
                    .compact();

            token = new Token(jwtToken);
        }

        return token;
    }

    private static Date setExpirationTime() {
        long expirationTimeMillis = System.currentTimeMillis() + (60 * 60 * 1000);
        return new Date(expirationTimeMillis);
    }

    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = getClaimsFromToken(jwtToken);
            return true;
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new TokenValidationFailedException(exception.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        String username = (String) claims.get("userId");
        String authoritiesString = (String) claims.get("authorities");

        return new UsernamePasswordAuthenticationToken(username,
                null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString));
    }

    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
