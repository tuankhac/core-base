package com.vmo.core.configs.security.authen.extractor;

import com.vmo.core.common.CommonConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SecretKeyExtractor implements TokenExtractor {
    @Override
    public String extract(HttpServletRequest request) {
        String token = request.getHeader(CommonConstants.HEADER_SECRET_KEY);

        return token;
    }
}
