package com.traffgun.acc.dto.graph;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNodePositionRequest {
    @NotNull
    private Double x;
    @NotNull
    private Double y;
}