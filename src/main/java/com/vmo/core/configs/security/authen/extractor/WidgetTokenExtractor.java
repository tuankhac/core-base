package com.vmo.core.configs.security.authen.extractor;

import com.vmo.core.common.CommonConstants;
import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.configs.CommonConfig;
import com.vmo.core.configs.env.Environments;
import com.vmo.core.configs.security.exception.NoAuthorizationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WidgetTokenExtractor implements TokenExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(WidgetTokenExtractor.class);
    @Autowired
    private CommonConfig commonConfig;

    @Override
    public String extract(HttpServletRequest request) {
        String tokenPayload = request.getHeader(CommonConstants.HEADER_AUTH_NAME);
        if (commonConfig.getEnv() == Environments.LOCAL
                || commonConfig.getEnv() == Environments.UNIT_TEST
        ) {
            LOG.info("attemptAuthentication tokenPayload: " + tokenPayload);
        }

        if (StringUtils.isBlank(tokenPayload)) {
            throw new NoAuthorizationException(CommonResponseMessages.MUST_LOGIN);
        }
        if (tokenPayload.length() < CommonConstants.HEADER_AUTH_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return tokenPayload.substring(CommonConstants.HEADER_AUTH_PREFIX.length());
    }
}
