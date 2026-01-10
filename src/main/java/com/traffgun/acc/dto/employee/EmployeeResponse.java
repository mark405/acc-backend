package com.traffgun.acc.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String comment;
    private Double rating;
    private Double qfd;
}
