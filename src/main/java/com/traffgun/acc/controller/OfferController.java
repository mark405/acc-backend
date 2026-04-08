package com.traffgun.acc.controller;

import com.traffgun.acc.dto.offer.UpdateOfferRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.Offer;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public Page<Offer> findAll(
            @RequestParam(required = false) String geo,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String name,
            @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        return offerService.findAll(geo, source, name, sortBy, direction, page, size);
    }

    @GetMapping("/{id}")
    public Offer findById(@PathVariable Long id) {
        return offerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    @PostMapping
    public Offer create(@RequestBody Offer offer) {
        return offerService.create(offer);
    }

    @PutMapping("/{id}")
    public Offer update(@PathVariable Long id, @RequestBody UpdateOfferRequest request) {
        Offer offer = offerService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return offerService.update(offer, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        offerService.delete(id);
    }
}