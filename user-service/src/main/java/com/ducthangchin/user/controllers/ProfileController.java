package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.opal.OpalClient;
import com.ducthangchin.commons.models.opal.Action;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.commons.models.opal.Resource;
import com.ducthangchin.commons.models.opal.ResourceType;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.user.entities.VDTUser;
import com.ducthangchin.user.models.ProfileUpdateRequest;
import com.ducthangchin.user.services.UserService;
import com.ducthangchin.user.utils.DTOConverter;
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
    private final OpalClient opalCLient;
    private final DTOConverter dtoConverter;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getALlProfiles(@RequestHeader("X-user-id") Long userId) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.read)
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

        return ResponseEntity.ok(
                userService.getAllUsers()
                        .stream()
                        .map(dtoConverter::convertToDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long id) {
        VDTUser user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dtoConverter.convertToDTO(user));
    }

    //update profile
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id,
            @RequestBody ProfileUpdateRequest profile
    ) {
        VDTUser existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.update)
                    .resource(new Resource(ResourceType.profile, id))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        userService.updateUserProfile(existingUser, profile);
        return ResponseEntity.ok(dtoConverter.convertToDTO(existingUser));
    }
}