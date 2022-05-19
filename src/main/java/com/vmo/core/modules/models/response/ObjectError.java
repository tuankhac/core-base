package com.vmo.core.modules.models.response;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

public class ObjectError {
    public static final int ERROR_PARAM = 100;
    public static final int ERROR_NOT_FOUND = 101;
    public static final int ERROR_DELETE = 102;
    public static final int FORMAT_TYPE = 103;

    public static final int STATUS_CODE_TOKEN_IN_VALID = 104;
    public static final int STATUS_CODE_EXPIRED_TOKEN = 105;
    public static final int STATUS_CODE_USERNAME_OR_PASSWORD_INVALID = 106;
    public static final int ERROR_REST_TEMPLATE = 107;

    public static final int NO_PERMISSION = 108;

    public static final int INVALID_ACTION = 109;

    public static final int UNSATISFIED_CONDITION = 110;

    public static final int UNKNOWN_ERROR = 999;

    private int errorCode;
    private String message;
    private LocalDateTime time;
    private String path;
    @Getter @Setter
    private String httpMethod;

    public ObjectError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
