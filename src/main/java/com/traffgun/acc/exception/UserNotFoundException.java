package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String username;

    public UserNotFoundException(String username) {
        super("NOT_FOUND");
        this.username = username;
    }
}
