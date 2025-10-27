package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class InvalidAccessTokenException extends RuntimeException {
    private final String message = "INVALID_ACCESS_TOKEN";
}
