package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS");
    }
}
