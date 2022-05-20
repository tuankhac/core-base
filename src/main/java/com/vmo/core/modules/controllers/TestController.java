package com.vmo.core.modules.controllers;

import com.vmo.core.common.BaseEndpoints;
import com.vmo.core.common.i18n.ApiLocaleService;
import com.vmo.core.common.i18n.LocaleService;
import com.vmo.core.configs.security.annotation.Authorized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private LocaleService localeService;

    @Autowired
    private ApiLocaleService apiLocaleService;

    private Logger logger = LogManager.getLogger(getClass());
    @Authorized(isRequired = false)
    @GetMapping(value = {
            BaseEndpoints.LOG_PUBLIC
    })
    public ResponseEntity checkStatusConnection(@RequestParam String content) throws Exception {
        String code = localeService.getMessage("hello");
        System.out.println("I18n " + code);

        code = apiLocaleService.getMessage("hello");
        System.out.println("APi I18n " + code);
        logger.info("dadandsda");
        return ResponseEntity.ok(content);
    }
}
