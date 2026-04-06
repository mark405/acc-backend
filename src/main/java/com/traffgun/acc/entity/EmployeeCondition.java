package com.traffgun.acc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(columnDefinition = "TEXT")
    private String text;
}
