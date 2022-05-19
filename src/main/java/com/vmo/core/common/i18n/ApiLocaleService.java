package com.vmo.core.common.i18n;

import javax.servlet.http.HttpServletRequest;

public interface ApiLocaleService {

    String getMessage(String code);

    String getMessage(String code, HttpServletRequest request);

    String getMessage(String code, String[] args);
}
