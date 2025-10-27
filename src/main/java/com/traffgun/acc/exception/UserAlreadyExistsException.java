package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String message = "USER_ALREADY_EXISTS";
}
