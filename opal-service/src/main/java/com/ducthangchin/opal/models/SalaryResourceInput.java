package com.ducthangchin.opal.models;

import com.ducthangchin.commons.models.opal.ResourceType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalaryResourceInput extends ResourceInput{
    private final ResourceType type = ResourceType.salary;
    private Long employee_id;
}
