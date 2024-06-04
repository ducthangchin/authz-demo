package com.ducthangchin.salary.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {
    @Id
    @SequenceGenerator(
            name="salary_id_sequence",
            sequenceName = "salary_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "salary_id_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private BigDecimal amount;

    private String currency = "VND";

    private String payPeriod;

    @Column(nullable = false)
    private Long createdBy;

    private String accountantName;

    private String note;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}