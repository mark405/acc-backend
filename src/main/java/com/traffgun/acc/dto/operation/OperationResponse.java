package com.traffgun.acc.dto.operation;

import com.traffgun.acc.dto.board.BoardResponse;
import com.traffgun.acc.dto.category.CategoryResponse;
import com.traffgun.acc.model.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OperationResponse {
    private Long id;
    private Double amount;
    private CategoryResponse category;
    private BoardResponse board;
    private String comment;
    private OperationType operationType;
    private Instant date;
}
