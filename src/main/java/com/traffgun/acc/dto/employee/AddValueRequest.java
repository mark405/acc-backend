package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddValueRequest {
    @NotNull
    private Long columnId;
    private String value;
}
