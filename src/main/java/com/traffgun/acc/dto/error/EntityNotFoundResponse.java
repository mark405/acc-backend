package com.traffgun.acc.dto.error;

import lombok.Getter;

@Getter
public class EntityNotFoundResponse extends ErrorResponse {
    private final Long id;

    public EntityNotFoundResponse(Long id, String message) {
        super(404, message);
        this.id = id;
    }
}
