package com.ducthangchin.commons.models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDetails {
    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;
    private List<Long> subordinateIds;
}
