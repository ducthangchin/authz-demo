package com.ducthangchin.commons.models.opal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OpalRequest {
    private Long userId;
    private Action action;
    private Resource resource;
}
