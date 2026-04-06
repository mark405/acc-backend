package com.traffgun.acc.dto.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EdgeResponse {
    public Long id;
    public Long source;
    public Long target;
}