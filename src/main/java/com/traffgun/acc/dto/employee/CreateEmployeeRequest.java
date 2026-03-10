package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEmployeeRequest {
    @NotNull
    private Long projectId;
    @NotNull
    private String name;
    @NotNull
    private Double qfd;
}
