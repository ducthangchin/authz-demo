package com.ducthangchin.clientfeign.user;

import com.ducthangchin.commons.models.UserDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER")
public interface AuthClient {
    // extract-claims
    @GetMapping(value = "/api/v1/user/auth/extract-claims")
    public ResponseEntity<UserDetails> extractClaims(@RequestHeader("Authorization") String token);
    // health check
    @GetMapping(value = "/api/v1/user/auth/health-check")
    public ResponseEntity<String> healthCheck();
}
