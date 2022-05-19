package com.vmo.core.configs.security.authen.secret;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class KeyAPIAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof KeyAPIAuthentication) {
            KeyAPIAuthentication keyAPI = (KeyAPIAuthentication) authentication;
            return new UsernamePasswordAuthenticationToken(keyAPI.getCredentials(), null, new ArrayList<>());
        }

        System.out.println("null key authentication on provider");
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return KeyAPIAuthentication.class.isAssignableFrom(authentication);
    }
}
