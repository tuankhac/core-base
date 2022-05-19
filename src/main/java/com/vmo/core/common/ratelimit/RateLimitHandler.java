package com.vmo.core.common.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.utils.CommonUtils;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.ErrorResponse;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RateLimitHandler extends HandlerInterceptorAdapter {
    /**
     * identity - requests
     */
    private static Map<String, List<ClientRequest>> clientRequests = new HashedMap<>();
    private Logger LOG = LoggerFactory.getLogger(RateLimitHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                ApiRateLimit rateLimit = method.getAnnotation(ApiRateLimit.class);

                if (rateLimit != null && rateLimit.isEnable()) {
                    String cookie = request.getHeader("Cookie");

                    if (StringUtils.isBlank(cookie)) {
                        if (rateLimit.requireCookie()) {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            ErrorResponse error = new ErrorResponse();
                            error.setTime(LocalDateTime.now());
                            error.setPath(request.getServletPath());
                            objectMapper.writeValue(response.getWriter(), error);

                            return false;
                        }
                        if (rateLimit.identity() == RateLimitIdentityMode.COOKIE) {
                            return false;
                        }
                    }

                    String sha1 = null;
                    if (rateLimit.identity() == RateLimitIdentityMode.COOKIE) {
                        MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
                        msdDigest.update(cookie.getBytes("UTF-8"), 0, cookie.length());
                        sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
                    }

                    String clientIP = CommonUtils.getClientIP(request);

                    if (StringUtils.isAllBlank(sha1, clientIP)) {
                        return true;
                    }

                    String identity = rateLimit.identity() == RateLimitIdentityMode.COOKIE
                            ? sha1
                            : (rateLimit.identity() == RateLimitIdentityMode.IP ? clientIP : null);
                    if (StringUtils.isBlank(identity)) {
                        LOG.warn("Could not detect identity for handling rate limit.");
                        return true;
                    }

                    if (rateLimit.whitelist().length > 0) {
                        for (String exclude : rateLimit.whitelist()) {
                            if (StringUtils.isNotBlank(exclude) && exclude.equalsIgnoreCase(identity)) {
                                return true;
                            }
                        }
                    }

                    ClientRequest client = null;
                    synchronized (handler) {
                        if (clientRequests.containsKey(identity)) {
                            client = clientRequests.get(identity).stream()
                                    .filter(c -> c.getApiHandler().equals(handler))
                                    .findFirst().orElse(null);
                        } else {
                            clientRequests.put(identity, new ArrayList<>());
                        }
                        if (client == null) {
                            client = new ClientRequest();
                            client.setStartCycleTime(LocalDateTime.now());
                            client.setApiHandler(handler);

                            clientRequests.get(identity).add(client);
                        }

                        if (Seconds.secondsBetween(client.getStartCycleTime(), LocalDateTime.now()).getSeconds() > rateLimit.duration()) {
                            client.setStartCycleTime(LocalDateTime.now());
                            client.setCount(0);
                        }

                        client.setCount(client.getCount() + 1);
                    }

                    if (client.getCount() > rateLimit.maxLimit()) {
                        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                        ErrorResponse error = new ErrorResponse(
                                "You are being rate limited. Please try again later.",
                                ErrorCode.RATE_LIMIT_EXCEED
                        );
                        error.setTime(LocalDateTime.now());
                        error.setPath(request.getServletPath());
                        objectMapper.writeValue(response.getWriter(), error);
                        LOG.info("Blocked request from [" + identity + "]. Hit count: " + client.getCount());

                        return false;
                    }
                }
            }
        } catch (RuntimeException | UnsupportedEncodingException | NoSuchAlgorithmException e) {}

        return true;
    }
}
