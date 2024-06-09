package com.ducthangchin.commons.models.opal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Resource {
    private ResourceType type;
    private Long id;

    public Resource(ResourceType resourceType) {
        this.type = resourceType;
    }
}