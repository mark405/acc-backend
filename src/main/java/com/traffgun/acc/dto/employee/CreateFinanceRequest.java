package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateFinanceRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Double incomeQFD;
    @NotNull
    private Double paidRef;
    @NotNull
    private Double percentQFD;
}
