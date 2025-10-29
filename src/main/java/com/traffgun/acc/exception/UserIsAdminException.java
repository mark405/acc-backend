package com.traffgun.acc.exception;

import lombok.Getter;

@Getter
public class UserIsAdminException extends RuntimeException {
    public UserIsAdminException() {
        super("USER_IS_ADMIN");
    }
}
