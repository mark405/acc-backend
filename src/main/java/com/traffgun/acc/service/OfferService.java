package com.traffgun.acc.service;

import com.traffgun.acc.dto.offer.UpdateOfferRequest;
import com.traffgun.acc.entity.Offer;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.repository.OfferRepository;
import com.traffgun.acc.specification.OfferSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    @Transactional(readOnly = true)
    public Page<Offer> findAll(String geo, String source, String name, String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Offer> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(OfferSpecification.hasGeo(geo))
                .and(OfferSpecification.hasSource(source))
                .and(OfferSpecification.hasName(name));

        return offerRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Offer> findById(Long id) {
        return offerRepository.findById(id);
    }

    @Transactional
    public Offer create(Offer offer) {
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer update(Offer offer, UpdateOfferRequest request) {
        offer.setName(request.getName());
        offer.setSource(request.getSource());
        offer.setGeo(request.getGeo());
        offer.setDescription(request.getDescription());
        return offerRepository.save(offer);
    }

    @Transactional
    public void delete(Long id) {
        offerRepository.deleteById(id);
    }
}
