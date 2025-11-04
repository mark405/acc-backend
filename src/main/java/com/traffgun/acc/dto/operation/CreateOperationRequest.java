package com.traffgun.acc.dto.operation;

import com.traffgun.acc.model.OperationType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateOperationRequest {
    @NotBlank
    private String name;
    @NotNull
    private Double amount;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long boardId;
    private String comment;
    @NotNull
    private OperationType operationType;
    @Nullable
    private Instant date;
}
