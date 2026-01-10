package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    public EmployeeResponse toDto(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getComment(),
                employee.getRating(),
                employee.getQfd()
        );
    }
}
