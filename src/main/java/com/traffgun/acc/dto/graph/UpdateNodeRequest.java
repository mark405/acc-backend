package com.traffgun.acc.dto.graph;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNodeRequest {
    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotNull
    private String color;
}