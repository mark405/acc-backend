package com.traffgun.acc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private final String username;
    private final String message = "NOT_FOUND";
}
