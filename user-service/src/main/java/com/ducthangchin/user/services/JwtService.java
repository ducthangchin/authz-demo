package com.ducthangchin.user.services;

import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.entities.VDTUser;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    private final JwtUtils jwtUtils;

    public JwtService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String generateAccessToken(VDTUser user) {
        return generateToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(VDTUser user) {
        return generateToken(user, refreshTokenExpiration);
    }

    public boolean isValidToken(String jwtToken) {
        return jwtUtils.isValidToken(jwtToken, jwtSecret);
    }

    public boolean hasTokenExpired(String jwtToken) {
        return jwtUtils.hasTokenExpired(jwtToken, jwtSecret);
    }

    public UserDetails extractClaims(String jwtToken) {
        return jwtUtils.extractClaims(jwtToken, jwtSecret);
    }

    private String generateToken(VDTUser user, long expiration) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("fullName", user.getFullName())
                .claim("roles", user.getRoles().stream().map(VDTRole::getRoleName).collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
