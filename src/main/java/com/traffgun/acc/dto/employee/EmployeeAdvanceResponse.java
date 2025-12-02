package com.traffgun.acc.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class EmployeeAdvanceResponse {
    private Long id;
    private Instant date;
    private Double amount;
}
