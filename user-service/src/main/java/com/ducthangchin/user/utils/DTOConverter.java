package com.ducthangchin.user.utils;

import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.entities.VDTUser;

import java.util.stream.Collectors;

public class DTOConverter {
    public UserDTO convertToDTO(VDTUser vdtUser) {
        return UserDTO.builder()
                .id(vdtUser.getId())
                .email(vdtUser.getEmail())
                .fullName(vdtUser.getFullName())
                .roles(vdtUser.getRoles().stream().map(VDTRole::getRoleName).collect(Collectors.toList()))
                .managerId(vdtUser.getManager() != null ? vdtUser.getManager().getId() : null)
                .subordinateIds(vdtUser.getSubordinates().stream().map(VDTUser::getId).collect(Collectors.toList()))
                .build();
    }
}
