package com.vmo.core.configs.security.exception;

import org.springframework.security.core.AuthenticationException;

public class NoAuthorizationException extends AuthenticationException {
    public NoAuthorizationException(String msg) {
        super(msg);
    }
}
