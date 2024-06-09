package com.ducthangchin.commons.models.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;
    private Long managerId;
    private List<Long> subordinateIds;
}
