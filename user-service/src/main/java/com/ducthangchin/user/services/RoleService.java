package com.ducthangchin.user.services;

import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.repositories.VDTRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RoleService {
    public final VDTRoleRepository vdtRoleRepository;

    public void deleteRole(Long id) {
        vdtRoleRepository.deleteById(id);
    }

    public VDTRole createRole(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Role name is required");
        }
        if (vdtRoleRepository.existsByRoleName(name)) {
            throw new IllegalArgumentException("Role already exists");
        }
        return vdtRoleRepository.save(new VDTRole(null, name));
    }

    public List<VDTRole> getAllRoles() {
        return vdtRoleRepository.findAll();
    }
}
