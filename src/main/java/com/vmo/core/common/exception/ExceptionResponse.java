package com.vmo.core.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.ObjectError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ExceptionResponse extends Exception {
    @Getter
    private ErrorCode errorCode;
    @JsonIgnore
    private HttpStatus httpStatus;

    public ExceptionResponse(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ExceptionResponse(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
        this.status = status; //old model //TODO remove
    }

    public HttpStatus getHttpStatus() {
        if (errorCode !=  null) {
            return errorCode.getHttpStatus();
        }
        return httpStatus;
    }

    private HttpStatus status;
    private ObjectError errorObject;

    public ExceptionResponse() {}

    @Deprecated
    public ExceptionResponse(int errorCode, String message, HttpStatus status) {
        super(message);
        this.errorObject = new ObjectError(errorCode, message);
        this.status = status;
    }

    @Deprecated
    public ExceptionResponse(ObjectError errorObject, HttpStatus status) {
        super(errorObject.getMessage());
        this.errorObject = errorObject;
        this.status = status;
    }

//    public ExceptionResponse(String message, HttpStatus status) {
//        super(message);
//        this.status = status;
//    }

    public HttpStatus getStatus() {
        return status;
    }

    public ObjectError getErrorObject() {
        return errorObject;
    }
}
