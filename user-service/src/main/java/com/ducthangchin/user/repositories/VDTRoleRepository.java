package com.ducthangchin.user.repositories;

import com.ducthangchin.user.entities.VDTRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VDTRoleRepository extends JpaRepository<VDTRole, Long> {
    VDTRole findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}
