package com.traffgun.acc.dto.operation;

import com.traffgun.acc.model.OperationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class UpdateOperationRequest {
    @NotNull
    private Double amount;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long boardId;
    private String comment;
    @NotNull
    private OperationType operationType;
    @NotNull
    private Instant date;
}
