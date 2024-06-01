package com.ducthangchin.user.controllers;

import com.ducthangchin.clientfeign.opal.OpalCLient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.commons.models.opal.OpalUserInput;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.models.RoleNameRequest;
import com.ducthangchin.user.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/roles")
@AllArgsConstructor
public class RoleController {
    private final OpalCLient opalCLient;
    private final RoleService roleService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<VDTRole> createRole(@RequestHeader("Authorization") String token, @RequestBody RoleNameRequest roleName) {
        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        OpalRequest opalRequest = OpalRequest.builder()
                .user(new OpalUserInput(userDetails))
                .build();
        if (!opalCLient.allow(opalRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(roleService.createRole(roleName.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VDTRole> deleteRole(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        OpalRequest opalRequest = OpalRequest.builder()
                .user(new OpalUserInput(userDetails))
                .build();
        if (!opalCLient.allow(opalRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    // get all
    @GetMapping
    public ResponseEntity<List<VDTRole>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

}
