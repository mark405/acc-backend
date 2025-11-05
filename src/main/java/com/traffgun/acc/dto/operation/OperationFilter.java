package com.traffgun.acc.dto.operation;

import com.traffgun.acc.model.OperationType;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class OperationFilter {

    private Long boardId;
    private OperationType type;
    @Nullable
    private Set<Long> categoryIds;
    @Nullable
    private String comment;
    @Nullable
    private Instant startDate;
    @Nullable
    private Instant endDate;

    private String sortBy = "id";
    private String direction = "desc";
    private int page = 0;
    private int size = 25;
}
