package com.ducthangchin.user.services;

import com.ducthangchin.user.entities.VDTUser;
import com.ducthangchin.user.repositories.VDTUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@AllArgsConstructor
public class UserService {
    public final VDTUserRepository vdtUserRepository;

    public VDTUser getUserById(Long id) {
        return vdtUserRepository.findById(id).orElse(null);
    }

    public VDTUser getUserRoles(Long id) {
        return vdtUserRepository.findById(id).orElse(null);
    }


}
