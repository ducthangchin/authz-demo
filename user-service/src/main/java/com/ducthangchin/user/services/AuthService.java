package com.ducthangchin.user.services;

import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.entities.VDTUser;
import com.ducthangchin.user.models.AuthRequest;
import com.ducthangchin.user.models.AuthResponse;
import com.ducthangchin.user.models.UserRegistrationForm;
import com.ducthangchin.user.models.UserRegistrationResponse;
import com.ducthangchin.user.repositories.VDTRoleRepository;
import com.ducthangchin.user.repositories.VDTUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthService {
    private final VDTUserRepository vdtUserRepository;
    private final VDTRoleRepository vdtRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserRegistrationResponse register(UserRegistrationForm userRegistrationForm) {
        // Fetch roles based on role IDs from the form
        List<VDTRole> roles = userRegistrationForm.getRoles().stream()
                .map(roleId -> vdtRoleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + roleId)))
                .collect(Collectors.toList());

        // Create a new VDTUser entity
        VDTUser newUser = new VDTUser();
        newUser.setFullName(userRegistrationForm.getFullName());
        newUser.setEmail(userRegistrationForm.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRegistrationForm.getPassword()));
        newUser.setRoles(roles);

        // Save the new user to the repository
        vdtUserRepository.saveAndFlush(newUser);

        // Create a response DTO
        return UserRegistrationResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .fullName(newUser.getFullName())
                .roles(newUser.getRoles().stream().map(VDTRole::getRoleName).collect(Collectors.toList()))
                .build();
    }

    public AuthResponse login(AuthRequest authRequest) throws IllegalArgumentException {
        // Authenticate user
        VDTUser user = vdtUserRepository.findByEmail(authRequest.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password!");
        }

        // Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        UserDetails userDetails = UserDetails.builder()
                .id(user.getId())
                .roles(user.getRoles().stream().map(VDTRole::getRoleName).collect(Collectors.toList()))
                .email(user.getEmail())
                .fullName(user.getFullName())
                .subordinateIds(user.getSubordinates().stream().map(VDTUser::getId).collect(Collectors.toList()))
                .build();

        // Create AuthResponse
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userDetails)
                .build();

        return authResponse;
    }


}
