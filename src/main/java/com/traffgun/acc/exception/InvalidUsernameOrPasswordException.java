package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class InvalidUsernameOrPasswordException extends RuntimeException {
    private final String message = "INVALID_USERNAME_OR_PASSWORD";
}
