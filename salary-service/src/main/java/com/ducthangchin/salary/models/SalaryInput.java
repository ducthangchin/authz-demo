package com.ducthangchin.salary.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryInput {
    private Long employeeId;
    private BigDecimal amount;
    private String currency;
    private String note;
    private String payPeriod;
}
