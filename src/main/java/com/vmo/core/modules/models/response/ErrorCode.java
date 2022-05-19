package com.vmo.core.modules.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    NO_PERMISSION(HttpStatus.FORBIDDEN),

    NO_VERIFIED_PHONE(HttpStatus.FORBIDDEN),
    ACCOUNT_STRIPE_NOT_EXIST(HttpStatus.BAD_REQUEST),

    NOT_IMPLEMENTED_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED),
    INVALID_PARAM(HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_FORMAT_TYPE(HttpStatus.UNPROCESSABLE_ENTITY),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    RESOURCE_DELETED(HttpStatus.NOT_FOUND),
    RESOURCE_DUPLICATE(HttpStatus.CONFLICT),

    INVALID_ACTION(HttpStatus.BAD_REQUEST),
    UNSATISFIED_CONDITION(HttpStatus.UNPROCESSABLE_ENTITY),

    RATE_LIMIT_EXCEED(HttpStatus.TOO_MANY_REQUESTS),
    SERVICE_TEMPORARY_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE),

    EXTERNAL_SERVICE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR),


    UNCATEGORIZED_CLIENT_ERROR(HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    @Getter
    private String code;
    @Getter
    @JsonIgnore
    private HttpStatus httpStatus;

    ErrorCode(HttpStatus status) {
        this.httpStatus = status;
        this.code = this.name();
    }
}
