package com.vmo.core.configs.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailException extends AuthenticationException {
    public AuthenticationFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationFailException(String msg) {
        super(msg);
    }
}
