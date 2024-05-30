package com.ducthangchin.user.controllers;

import com.ducthangchin.user.entities.VDTRole;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/roles")
@AllArgsConstructor
public class RoleController {
    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestBody VDTRole role) {
        return ResponseEntity.ok("All goes well");
    }
}
