package com.vmo.core.common.exception;

import com.vmo.core.modules.models.response.ObjectError;
import org.springframework.http.HttpStatus;

public class ExceptionNumberFormatRequest extends RuntimeException {
    private HttpStatus status;
    private ObjectError errorObject;

    public ExceptionNumberFormatRequest(ObjectError errorObject, HttpStatus status) {
        super("");
        this.errorObject = errorObject;
        this.status = status;
    }
    public ExceptionNumberFormatRequest(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ObjectError getErrorObject() {
        return errorObject;
    }
}