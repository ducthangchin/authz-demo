package com.ducthangchin.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationResponse {
    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;
}