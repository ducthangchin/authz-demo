package com.ducthangchin.commons.models.opal;

import com.ducthangchin.commons.models.UserDetails;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OpalUserInput {
    private Long id;
    private Long manager_id;
    private List<String> roles;
    private List<Long> subordinate_ids;

    public OpalUserInput(UserDetails userDetails) {
        this.id = userDetails.getId();
        this.roles = userDetails.getRoles();
        this.subordinate_ids = userDetails.getSubordinateIds();
    }
}
