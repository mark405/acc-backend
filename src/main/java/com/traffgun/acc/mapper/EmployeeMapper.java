package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.dto.register.RegisterRequest;
import com.traffgun.acc.dto.register.RegisterResponse;
import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final UserMapper userMapper;

    public EmployeeResponse toDto(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getComment(),
                employee.getRating()
        );
    }
}
