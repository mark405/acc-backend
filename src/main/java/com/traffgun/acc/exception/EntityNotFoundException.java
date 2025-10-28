package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Long id;

    public EntityNotFoundException(Long id) {
        super("NOT_FOUND");
        this.id = id;
    }
}
