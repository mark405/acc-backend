package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateAdvanceRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private Long projectId;
    private Instant date;
    private Double amount;
}
