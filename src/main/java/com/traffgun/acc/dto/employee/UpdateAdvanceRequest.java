package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class UpdateAdvanceRequest {
    @NotNull
    private Instant date;
    @NotNull
    private Double amount;
}
