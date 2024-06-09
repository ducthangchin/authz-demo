package com.ducthangchin.opal.models;

import com.ducthangchin.commons.models.dto.UserDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserInput {
    private Long id;
    private List<String> roles;
    private Long manager_id;
    private List<Long> subordinate_ids;

    public UserInput(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.roles = userDTO.getRoles();
        this.manager_id = userDTO.getManagerId();
        this.subordinate_ids = userDTO.getSubordinateIds();
    }
}
