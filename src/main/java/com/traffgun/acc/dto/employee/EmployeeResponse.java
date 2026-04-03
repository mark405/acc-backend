package com.traffgun.acc.dto.employee;

import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.model.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String comment;
    private Double rating;
    private EmployeeRole role;
    private UserResponse user;
    private List<ColumnResponse> columns;
}
