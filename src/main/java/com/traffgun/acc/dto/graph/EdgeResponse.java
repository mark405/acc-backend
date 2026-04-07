package com.traffgun.acc.dto.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EdgeResponse {
    private Long id;
    private Long source;
    private Long target;
    private String sourceHandle;
    private String targetHandle;
}