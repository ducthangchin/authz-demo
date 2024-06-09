package com.ducthangchin.opal.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResourceInput {
    private Long id;
    private Long created_by;
}
