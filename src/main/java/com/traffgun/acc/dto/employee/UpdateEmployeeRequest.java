package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEmployeeRequest {
    @NotNull
    private Double qfd;
}
