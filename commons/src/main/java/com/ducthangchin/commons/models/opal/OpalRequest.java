package com.ducthangchin.commons.models.opal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OpalRequest {
    private OpalUserInput user;
    private Action action;
    private ResourceInput resource;
}
