package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.history.HistoryResponse;
import com.traffgun.acc.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryMapper {
    private final EmployeeMapper employeeMapper;

    public HistoryResponse toDto(History history) {
        return new HistoryResponse(
                history.getId(),
                history.getBody(),
                employeeMapper.toDto(history.getEmployee()),
                history.getType(),
                history.getDate()
        );
    }
}
