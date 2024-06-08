package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.opal.OpalCLient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.commons.models.opal.OpalUserInput;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.user.dto.UserDTO;
import com.ducthangchin.user.entities.VDTUser;
import com.ducthangchin.user.models.ProfileUpdateRequest;
import com.ducthangchin.user.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user/profile")
@AllArgsConstructor
@Slf4j
public class ProfileController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final OpalCLient opalCLient;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getALlProfiles(@RequestHeader("Authorization") String token) {
        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        OpalRequest opalRequest = OpalRequest.builder()
                .user(new OpalUserInput(userDetails))
                .build();

        log.info("Allowing Opal request: {}", opalRequest);

        if (!opalCLient.allow(opalRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                userService.getAllUsers()
                        .stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getProfile(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        VDTUser user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new UserDTO(user));
    }

    //update profile
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody ProfileUpdateRequest profile) {
        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        OpalRequest opalRequest = OpalRequest.builder()
                .user(new OpalUserInput(userDetails))
                .build();

        log.info("Allowing Opal request: {}", opalRequest);

        if (!opalCLient.allow(opalRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        VDTUser existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        userService.updateUserProfile(existingUser, profile);
        return ResponseEntity.ok(new UserDTO(existingUser));
    }
}
