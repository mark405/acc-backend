package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.employee.EmployeeAdvanceResponse;
import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeAdvance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeAdvanceMapper {
    public EmployeeAdvanceResponse toDto(EmployeeAdvance advance) {
        return new EmployeeAdvanceResponse(
                advance.getId(),
                advance.getDate(),
                advance.getAmount()
        );
    }
}
