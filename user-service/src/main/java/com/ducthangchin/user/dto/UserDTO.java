package com.ducthangchin.user.dto;

import com.ducthangchin.user.entities.VDTRole;
import com.ducthangchin.user.entities.VDTUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;
    private Long managerId;
    private List<Long> subordinateIds;

    public UserDTO(VDTUser vdtUser) {
        this.id = vdtUser.getId();
        this.email = vdtUser.getEmail();
        this.fullName = vdtUser.getFullName();
        this.roles = vdtUser.getRoles().stream().map(VDTRole::getRoleName).collect(Collectors.toList());
        if (vdtUser.getManager() != null) {
            this.managerId = vdtUser.getManager().getId();
        }
        this.subordinateIds = vdtUser.getSubordinates().stream().map(VDTUser::getId).collect(Collectors.toList());
    }
}
