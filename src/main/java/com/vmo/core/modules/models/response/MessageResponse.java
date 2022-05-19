package com.vmo.core.modules.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class MessageResponse {
    @Getter
    private String message;
    @Getter
    @JsonIgnore
    private HttpStatus httpStatus;

    public MessageResponse() {}

    public MessageResponse(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }

    public MessageResponse(String message, HttpStatus status) {
        this.message = message;
        this.httpStatus = status;
    }
}
