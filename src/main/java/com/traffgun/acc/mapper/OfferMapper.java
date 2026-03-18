package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.offer.OfferResponse;
import com.traffgun.acc.entity.Offer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferMapper {
    public OfferResponse toDto(Offer offer) {
        return new OfferResponse(
                offer.getId(),
                offer.getName(),
                offer.getSource(),
                offer.getGeo(),
                offer.getDescription()
        );
    }
}
