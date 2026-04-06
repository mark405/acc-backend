package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditValueRequest {
    @NotNull
    private Long id;
    private Long columnId;
    private String value;
}
