package com.ducthangchin.user.services;

import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.entities.VDTUser;
import com.ducthangchin.user.models.ProfileUpdateRequest;
import com.ducthangchin.user.repositories.VDTRoleRepository;
import com.ducthangchin.user.repositories.VDTUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserService {
    public final VDTUserRepository vdtUserRepository;
    public final VDTRoleRepository vdtRoleRepository;

    public VDTUser getUserById(Long id) {
        return vdtUserRepository.findById(id).orElse(null);
    }

    public List<VDTUser> getAllUsers() {
        return vdtUserRepository.findAll();
    }

    public VDTUser getUserRoles(Long id) {
        return vdtUserRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateUserProfile(VDTUser user, ProfileUpdateRequest profile) {
        // Update fullName
        if (profile.getFullName() != null) {
            user.setFullName(profile.getFullName());
        }

        // Update manager
        if (profile.getManager() != null) {
            Optional<VDTUser> managerOpt = vdtUserRepository.findById(profile.getManager());
            if (managerOpt.isPresent()) {
                user.setManager(managerOpt.get());
            } else {
                // Handle manager not found
                throw new IllegalArgumentException("Manager not found with id: " + profile.getManager());
            }
        } else {
            user.setManager(null); // Remove manager if not specified
        }

        // Update roles
        if (profile.getRoles() != null) {
            Collection<VDTRole> roles = vdtRoleRepository.findAllById(List.of(profile.getRoles()));
            if (roles.size() != profile.getRoles().length) {
                // Handle some roles not found
                throw new IllegalArgumentException("Some roles not found");
            }
            user.setRoles(roles);
        }

        // Save the updated user
        vdtUserRepository.saveAndFlush(user);
    }


}
