package com.traffgun.acc.dto.graph;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddEdgeRequest {
    @NotNull
    private Long target;
    @NotNull
    private Long source;
}