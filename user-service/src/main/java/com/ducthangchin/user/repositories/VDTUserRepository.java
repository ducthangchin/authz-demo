package com.ducthangchin.user.repositories;

import com.ducthangchin.user.entities.VDTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VDTUserRepository extends JpaRepository<VDTUser, Long> {
    VDTUser findByEmail(String email);
    List<VDTUser> findAll();
}
