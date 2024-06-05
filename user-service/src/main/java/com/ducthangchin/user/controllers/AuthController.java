package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.opal.OpalCLient;
import com.ducthangchin.clientfeign.salary.SalaryClient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.commons.models.opal.OpalUserInput;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.user.models.AuthRequest;
import com.ducthangchin.user.models.AuthResponse;
import com.ducthangchin.user.models.UserRegistrationForm;
import com.ducthangchin.user.models.UserRegistrationResponse;
import com.ducthangchin.user.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final SalaryClient salaryClient;
    private final JwtUtils jwtUtils;
    private final OpalCLient opalCLient;

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
    public ResponseEntity<UserRegistrationResponse> register(
            @RequestHeader("Authorization") String token,
            @RequestBody UserRegistrationForm userRegistrationForm) {
        try {
            UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
            OpalRequest opalRequest = OpalRequest.builder()
                    .user(new OpalUserInput(userDetails))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
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
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder().build());
        }
    }

    // check is valid token
//    @GetMapping(value = "/validate-token")
//    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
//        log.info(jwtUtils.extractClaimsWithoutKey(token).toString());
//        return ResponseEntity.ok(jwtService.isValidToken(token));
//    }
//
//    @GetMapping(value = "/extract-claims")
//    public ResponseEntity<UserDetails> extractClaims(@RequestHeader("Authorization") String token) {
//        try {
//            return ResponseEntity.ok(jwtService.extractClaims(token));
//        } catch (Exception e) {
//            log.info(e.getMessage());
//            return ResponseEntity.internalServerError().body(UserDetails.builder().build());
//        }
//    }
}
