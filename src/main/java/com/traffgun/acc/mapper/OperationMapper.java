package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.operation.OperationResponse;
import com.traffgun.acc.entity.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperationMapper {
    private final CategoryMapper categoryMapper;
    private final BoardMapper boardMapper;

    public OperationResponse toDto(Operation operation) {
        return new OperationResponse(
                operation.getId(),
                operation.getAmount(),
                categoryMapper.toDto(operation.getCategory()),
                boardMapper.toDto(operation.getBoard()),
                operation.getComment(),
                operation.getOperationType(),
                operation.getDate()
        );
    }
}
