package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class InvalidRefreshTokenException extends RuntimeException {
    private final String message = "INVALID_REFRESH_TOKEN";
}
