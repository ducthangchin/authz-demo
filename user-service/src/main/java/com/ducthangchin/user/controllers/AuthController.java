package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.opal.OpalClient;
import com.ducthangchin.clientfeign.salary.SalaryClient;
import com.ducthangchin.commons.models.opal.Action;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.commons.models.opal.Resource;
import com.ducthangchin.commons.models.opal.ResourceType;
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
    private final OpalClient opalCLient;

    @GetMapping(value = "/health-check")
    public ResponseEntity<String> healthCheck() {
        try {
            String salaryServiceHealthCheck = salaryClient.healthCheck();
            return ResponseEntity.ok("User authentication service is up and running\n" +
                    salaryServiceHealthCheck);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body("User authentication service is down");
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserRegistrationResponse> register(
            @RequestHeader("X-user-id") Long userId,
            @RequestBody UserRegistrationForm userRegistrationForm
    ) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.create)
                    .resource(new Resource(ResourceType.profile))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        return ResponseEntity.ok(authService.register(userRegistrationForm));
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
}
