package com.traffgun.acc.dto.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeResponse {
    public Long id;
    public String type;
    public String name;
    public String role;
    public Double x;
    public Double y;
}