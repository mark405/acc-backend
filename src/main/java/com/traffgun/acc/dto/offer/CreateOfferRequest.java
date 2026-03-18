package com.traffgun.acc.dto.offer;

import lombok.Data;

@Data
public class CreateOfferRequest {
    private String name;
    private String geo;
    private String source;
    private String description;
}
