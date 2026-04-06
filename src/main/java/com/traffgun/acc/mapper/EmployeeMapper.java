package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.employee.ColumnResponse;
import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final UserMapper userMapper;

    public EmployeeResponse toDto(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getComment(),
                employee.getRating(),
                employee.getRole(),
                userMapper.toUserDto(employee.getUser()),
                employee.getColumns() == null ? new ArrayList<>() :
                employee.getColumns().stream().map(it -> new ColumnResponse(it.getId(), it.getName(), it.getIndex())).toList()
        );
    }
}
