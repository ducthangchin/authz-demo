package com.ducthangchin.commons.utils;

import com.ducthangchin.commons.models.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwtParser;

import java.util.Date;
import java.util.List;

public class JwtUtils {
    public boolean isValidToken(String jwtToken, String jwtSecret) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasTokenExpired(String jwtToken, String jwtSecret) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public UserDetails extractClaims(String jwtToken, String jwtSecret) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        return UserDetails.builder()
                .id(claims.get("id", Long.class))
                .email(claims.get("email", String.class))
                .fullName(claims.get("fullName", String.class))
                .roles(claims.get("roles", List.class))
                .build();
    }

    public UserDetails extractClaimsWithoutKey(String token) {
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";
        DefaultJwtParser parser = new DefaultJwtParser();
        Claims claims = parser.parseClaimsJwt(unsignedToken).getBody();
        return UserDetails.builder()
                .id(claims.get("id", Long.class))
                .email(claims.get("email", String.class))
                .fullName(claims.get("fullName", String.class))
                .roles(claims.get("roles", List.class))
                .build();
    }
}
