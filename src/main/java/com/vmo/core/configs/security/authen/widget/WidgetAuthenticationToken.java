package com.vmo.core.configs.security.authen.widget;

import com.vmo.core.configs.security.context.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class WidgetAuthenticationToken extends AbstractAuthenticationToken {
    private final UserContext userContext;

    public WidgetAuthenticationToken(String token) {
        super(null);
        userContext = new UserContext();
        userContext.setToken(token);
    }

    public WidgetAuthenticationToken(UserContext userContext) {
        super(null);

        this.userContext = userContext;
        this.setAuthenticated(false);
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
