package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.employee.EmployeeAdvanceResponse;
import com.traffgun.acc.dto.employee.EmployeeFinanceResponse;
import com.traffgun.acc.entity.EmployeeAdvance;
import com.traffgun.acc.entity.EmployeeFinance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeFinanceMapper {
    public EmployeeFinanceResponse toDto(EmployeeFinance finance, List<EmployeeAdvanceResponse> advances) {
        return new EmployeeFinanceResponse(
                finance.getId(),
                finance.getStartDate(),
                finance.getEndDate(),
                finance.getIncomeQFD(),
                finance.getPaidRef(),
                finance.getPercentQFD(),
                advances
        );
    }
}
