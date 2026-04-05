package com.traffgun.acc.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnResponse {
    private Long id;
    private String name;
    private Integer index;
}
