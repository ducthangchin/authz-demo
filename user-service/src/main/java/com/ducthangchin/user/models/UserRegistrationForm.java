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
public class UserRegistrationForm {
    private String email;
    private String password;
    private String fullName;
    private List<Long> roles;
}
