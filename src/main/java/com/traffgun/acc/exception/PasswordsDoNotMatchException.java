package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("PASSWORDS_DO_NOT_MATCH");
    }
}
