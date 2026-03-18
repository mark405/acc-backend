package com.traffgun.acc.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferResponse {
    private Long id;
    private String name;
    private String geo;
    private String source;
    private String description;
}
