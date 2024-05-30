package com.ducthangchin.user.repositories;

import com.ducthangchin.user.entities.VDTUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VDTUserRepository extends JpaRepository<VDTUser, Long> {
    VDTUser findByEmail(String email);
}
