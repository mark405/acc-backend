package com.traffgun.acc.dto.employee;

import com.traffgun.acc.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String comment;
    private Double rating;
//    private UserResponse user;
}
