package com.vmo.core.modules.controllers;

import com.vmo.core.logging.log4j.Log4jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @Autowired
    Log4jService log4jService;

    @PostMapping(value = "/log")
    public ResponseEntity getMessageLog(@RequestBody String content) throws Exception {
        log4jService.log(content);
        return  ResponseEntity.ok(content);
    }
}
