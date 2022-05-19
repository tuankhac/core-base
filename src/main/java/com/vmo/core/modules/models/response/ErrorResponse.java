package com.vmo.core.modules.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.joda.time.LocalDateTime;

@JsonPropertyOrder({"errorCode", "message"})
@Data
public class ErrorResponse {
    @JsonIgnore
    private ErrorCode errorCode;
    private String message;
    private LocalDateTime time;
    private String path;
    private String httpMethod;

    public ErrorResponse() {}

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorResponse(String message, ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @JsonProperty("errorCode")
    public String getErrorCodeName() {
        return errorCode != null ? errorCode.getCode() : null;
    }
}
