package com.traffgun.acc.dto.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeResponse {
    private Long id;
    private String type;
    private String name;
    private String role;
    private Double x;
    private Double y;
    private String color;
}