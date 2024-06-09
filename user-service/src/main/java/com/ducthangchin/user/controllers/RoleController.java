package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.opal.OpalClient;
import com.ducthangchin.commons.models.opal.Action;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.commons.models.opal.Resource;
import com.ducthangchin.commons.models.opal.ResourceType;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.models.RoleNameRequest;
import com.ducthangchin.user.services.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/roles")
@AllArgsConstructor
@Slf4j
public class RoleController {
    private final OpalClient opalCLient;
    private final RoleService roleService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<VDTRole> createRole(
            @RequestHeader("X-user-id") Long userId,
            @RequestBody RoleNameRequest roleName
    ) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.create)
                    .resource(new Resource(ResourceType.role))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        return ResponseEntity.ok(roleService.createRole(roleName.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VDTRole> deleteRole(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id
    ) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.delete)
                    .resource(new Resource(ResourceType.role, id))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<VDTRole>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

}
