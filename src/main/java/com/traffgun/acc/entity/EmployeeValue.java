package com.traffgun.acc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeColumnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_finance_id", nullable = false)
    private EmployeeFinance finance;

    private String value;
}
