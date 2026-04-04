package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateFinanceRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private Long projectId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    private List<AddValueRequest> values;
}
