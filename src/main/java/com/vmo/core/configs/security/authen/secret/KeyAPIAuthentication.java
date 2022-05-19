package com.vmo.core.configs.security.authen.secret;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class KeyAPIAuthentication extends AbstractAuthenticationToken {
    private final String apiKey;

    public KeyAPIAuthentication(String apiKey) {
        super(null);
        this.apiKey = apiKey;
    }

    @Override
    public String getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
