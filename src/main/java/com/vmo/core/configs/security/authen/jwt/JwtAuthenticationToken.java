package com.vmo.core.configs.security.authen.jwt;

import com.vmo.core.configs.security.context.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final UserContext userContext;

    public JwtAuthenticationToken(String token) {
        super(null);
        userContext = new UserContext();
        userContext.setToken(token);
    }


    public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userContext = userContext;
    }

    @Override
    public UserContext getCredentials() {
        return userContext;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
