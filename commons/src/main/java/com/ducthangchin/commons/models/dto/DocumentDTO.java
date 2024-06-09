package com.ducthangchin.commons.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private Long id;
    private String name;
    private Boolean blocked;
    private Long createdBy;
    private String createdByUsername;
    private Date createdAt;
    private Date updatedAt;
}
