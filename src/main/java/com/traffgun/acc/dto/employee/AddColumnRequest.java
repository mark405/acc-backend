package com.traffgun.acc.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddColumnRequest {
    private String name;
    @NotNull
    private Integer index;
}
