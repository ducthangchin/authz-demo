package com.ducthangchin.user.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/profile")
@AllArgsConstructor
public class ProfileController {


    // get with path variable
    @GetMapping("/{id}")
    public String getProfile(Long id) {
        return "Profile";
    }
}
