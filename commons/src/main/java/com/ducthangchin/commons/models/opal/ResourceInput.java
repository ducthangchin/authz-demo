package com.ducthangchin.commons.models.opal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResourceInput {
    private ResourceType type;
    private Long id;
    private Long created_by;
    private boolean blocked;
}
