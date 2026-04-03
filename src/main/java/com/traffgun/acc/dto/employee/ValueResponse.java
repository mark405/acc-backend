package com.traffgun.acc.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValueResponse {
    private String name;
    private Long employeeColumnId;
}
