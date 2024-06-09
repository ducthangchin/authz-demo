package com.ducthangchin.opal.models;

import com.ducthangchin.commons.models.opal.Action;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OpalRequestInput {
    private UserInput user;
    private Action action;
    private ResourceInput resource;
}
