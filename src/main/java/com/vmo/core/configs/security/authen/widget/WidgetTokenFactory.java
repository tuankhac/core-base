package com.vmo.core.configs.security.authen.widget;

import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.configs.security.SecurityConfig;
import com.vmo.core.configs.security.context.UserContext;
import com.vmo.core.configs.security.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class WidgetTokenFactory {
    @Autowired
    private SecurityConfig webSecurityConfig;

    public UserContext parseToken(String token) {
        Jws<Claims> claimsJws = parseClaims(token);
        Claims body = claimsJws.getBody();
        String email = body.get("email", String.class);
        String id = body.get("_id", String.class);

        UserContext userContext = new UserContext(token);
        userContext.setPartnerId(id);
        userContext.setEmail(email);

        return userContext;
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            String jwtSecret = webSecurityConfig.getTokenSecret();
            return Jwts.parser().setSigningKey(
                    new String(Base64.getEncoder().encode(jwtSecret.getBytes()))).parseClaimsJws(
                    token
            );
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {

            ex.printStackTrace();
            throw new BadCredentialsException(CommonResponseMessages.INVALID_TOKEN, ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException(expiredEx.getMessage(), token, expiredEx);
        }
    }
}
