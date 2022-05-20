package com.vmo.core.modules.controllers;


import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.configs.security.annotation.Authorized;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
public class AuctionController {

    @GetMapping(value = "/api/auction")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query"),
    })
    @Authorized
    public ResponseEntity getAuctions(
//            @RequestParam(value = "startDate") @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME) LocalDateTime startDate,
//            @RequestParam(value = "endDate") @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "createdById", required = false) String createdById,
            Pageable page
    ) throws ExceptionResponse {

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}


