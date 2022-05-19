package com.vmo.core.configs.security.authen.widget;

import com.vmo.core.configs.security.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WidgetAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private WidgetTokenFactory widgetTokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof WidgetAuthenticationToken) {
            WidgetAuthenticationToken widgetToken = (WidgetAuthenticationToken) authentication;
            UserContext userContext = widgetTokenFactory.parseToken(widgetToken.getCredentials().getToken());
            return new UsernamePasswordAuthenticationToken(widgetToken.getCredentials(), null, new ArrayList<>());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WidgetAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
