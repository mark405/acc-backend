package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateFinanceRequest {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
