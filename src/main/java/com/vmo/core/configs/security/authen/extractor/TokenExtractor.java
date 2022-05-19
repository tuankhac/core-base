package com.vmo.core.configs.security.authen.extractor;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    String extract(HttpServletRequest request);
}
