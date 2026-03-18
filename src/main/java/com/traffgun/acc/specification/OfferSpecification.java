package com.traffgun.acc.specification;

import com.traffgun.acc.entity.Offer;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecification {

    public static Specification<Offer> hasGeo(String geo) {
        return (root, query, cb) ->
                geo == null || geo.isEmpty() ? null : cb.like(cb.lower(root.get("geo")), "%" + geo.toLowerCase() + "%");
    }

    public static Specification<Offer> hasSource(String source) {
        return (root, query, cb) ->
                source == null || source.isEmpty() ? null : cb.like(cb.lower(root.get("source")), "%" + source.toLowerCase() + "%");
    }

    public static Specification<Offer> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty() ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}