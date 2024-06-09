package com.ducthangchin.opal.models;

import com.ducthangchin.commons.models.opal.ResourceType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocumentResourceInput extends ResourceInput{
    private final ResourceType type = ResourceType.document;
    private boolean blocked;
}
