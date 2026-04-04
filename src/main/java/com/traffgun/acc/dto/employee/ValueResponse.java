package com.traffgun.acc.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValueResponse {
    private Long id;
    private String value;
    private Long employeeColumnId;
}
