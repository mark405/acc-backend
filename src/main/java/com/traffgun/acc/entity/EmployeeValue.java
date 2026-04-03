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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_column_id", nullable = false)
    private EmployeeColumn column;

    private String value;
}
