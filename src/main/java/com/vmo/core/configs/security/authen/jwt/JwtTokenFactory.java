package com.vmo.core.configs.security.authen.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.configs.security.SecurityConfig;
import com.vmo.core.configs.security.context.UserContext;
import com.vmo.core.configs.security.exception.JwtExpiredTokenException;
import com.vmo.core.modules.models.database.types.user.UserRoleType;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class JwtTokenFactory {
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private ObjectMapper objectMapper;

    public UserContext parseAccessToken(String token) {
        Jws<Claims> claimsJws = parseClaims(token);
        Claims body = claimsJws.getBody();
        String id = body.get("_id", String.class);
        String email = body.get("email", String.class);

        UserRoleType primaryRole = UserRoleType.findByValue(body.get("role", String.class));
        List<UserRoleType> userRoles = new ArrayList<>();
        userRoles.add(primaryRole);
        //roles is removed

        String partnerId = body.get("partner", String.class);
        String wholesalerId = body.get("wholesaler", String.class);
//        String onlineStoreId = body.get("online_store", String.class);
        String storefrontId = body.get("storefront", String.class);

        UserContext userContext = new UserContext(token);
        userContext.setId(id);
        userContext.setEmail(email);
        userContext.setUserRoles(userRoles);
        userContext.setPartnerId(partnerId);
        userContext.setWholesalerId(wholesalerId);
//        userContext.setOnlineStoreId(onlineStoreId);
        userContext.setStorefrontId(storefrontId);

        return userContext;
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(new String(Base64.getEncoder().encode(
                    securityConfig.getTokenSecret().getBytes()))
            ).parseClaimsJws(
                    token
            );
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {

            ex.printStackTrace();
            throw new BadCredentialsException(CommonResponseMessages.INVALID_TOKEN, ex);
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
            throw new JwtExpiredTokenException(ex.getMessage(), token, ex);
        }
    }
}
