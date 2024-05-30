package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.salary.SalaryClient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.user.models.AuthRequest;
import com.ducthangchin.user.models.AuthResponse;
import com.ducthangchin.user.models.UserRegistrationForm;
import com.ducthangchin.user.models.UserRegistrationResponse;
import com.ducthangchin.user.services.AuthService;
import com.ducthangchin.user.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final SalaryClient salaryClient;
    private final JwtUtils jwtUtils;

    @GetMapping(value = "/health-check")
    public ResponseEntity<String> healthCheck() {
        try {
            String salaryServiceHealthCheck = salaryClient.healthCheck();
            return ResponseEntity.ok("User authentication service is up and running\n" + salaryServiceHealthCheck);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body("User authentication service is down");
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationForm userRegistrationForm) {
        try {
            return ResponseEntity.ok(authService.register(userRegistrationForm));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(UserRegistrationResponse.builder().build());
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            return ResponseEntity.ok(authService.login(authRequest));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(AuthResponse.builder().build());
        }
    }

    // check is valid token
    @GetMapping(value = "/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        log.info(jwtUtils.extractClaimsWithoutKey(token).toString());
        return ResponseEntity.ok(jwtService.isValidToken(token));
    }

    @GetMapping(value = "/extract-claims")
    public ResponseEntity<UserDetails> extractClaims(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(jwtService.extractClaims(token));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(UserDetails.builder().build());
        }
    }

    @GetMapping(value = "/current-header")
    public Map<String, String> getCurrentHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        return headers;
    }

}
