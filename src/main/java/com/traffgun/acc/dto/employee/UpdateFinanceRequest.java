package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFinanceRequest {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    List<EditValueRequest> values;
}
