package com.vmo.core.configs.security.authen.jwt;

import com.vmo.core.common.CommonConstants;
import com.vmo.core.configs.security.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            UserContext userContext = jwtTokenFactory.parseAccessToken(jwtAuthenticationToken.getCredentials().getToken());
            Authentication authenResult = new UsernamePasswordAuthenticationToken(
                    userContext, null,
                    userContext.getPrimaryRole() != null
                            ? Collections.singleton(new SimpleGrantedAuthority(CommonConstants.ROLE_PREFIX + userContext.getPrimaryRole().name()))
                            : new ArrayList()
            );
            return authenResult;
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
