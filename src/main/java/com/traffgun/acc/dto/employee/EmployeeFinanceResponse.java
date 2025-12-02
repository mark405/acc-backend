package com.traffgun.acc.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeFinanceResponse {
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double incomeQFD;

    private Double paidRef;

    private Double percentQFD;

    private List<EmployeeAdvanceResponse> advances;
}
