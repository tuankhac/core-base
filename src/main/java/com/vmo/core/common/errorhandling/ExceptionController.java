package com.vmo.core.common.errorhandling;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.configs.CommonConfig;
import com.vmo.core.configs.http.CacheServletInputStream;
import com.vmo.core.logging.repositories.LogApiErrorRepository;
import com.vmo.core.modules.models.database.entities.shared.LogApiError;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.ErrorResponse;
import com.vmo.core.modules.models.response.ObjectError;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class ExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogApiErrorRepository logApiErrorRepository;

    //region Client errors
    @ExceptionHandler(ClientAbortException.class)
    public void handleDisconnected(HttpServletRequest request, HttpServletResponse response, ClientAbortException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                ErrorCode.UNCATEGORIZED_CLIENT_ERROR
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(request.getServletPath());
        errorResponse.setHttpMethod(request.getMethod());

//        increaseClientError(request, e);
        //non-standard status https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#Unofficial_codes
        //nginx status 499
//        return ResponseEntity.status(499).body(errorResponse);

        try {
            LOG.warn(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @ExceptionHandler
    public ResponseEntity handleWrongType(HttpServletRequest request, HttpMediaTypeNotAcceptableException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                ErrorCode.UNCATEGORIZED_CLIENT_ERROR
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(request.getServletPath());
        errorResponse.setHttpMethod(request.getMethod());

//        increaseClientError(request, e);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleWrongHttpMethod(HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ErrorCode.NOT_IMPLEMENTED_HTTP_METHOD
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());

//        increaseClientError(req, ex);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolation(
            HttpServletRequest req, ConstraintViolationException e
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                ErrorCode.INVALID_PARAM
        );

        if (CollectionUtils.isNotEmpty(e.getConstraintViolations())) {
            ConstraintViolation violation = (ConstraintViolation) e.getConstraintViolations().toArray()[0];
            Path paramPath = violation.getPropertyPath();
            if (paramPath != null) {
                String paramPathString = paramPath.toString();
                String param = paramPathString.contains(".")
                        ? paramPathString.substring(paramPathString.lastIndexOf(".") + 1)
                        : paramPathString;
                errorResponse = new ErrorResponse(
                        param + " is required",
                        ErrorCode.INVALID_PARAM
                );
            }
        }
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());

//        increaseClientError(req, e);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(
            HttpServletRequest req, HttpMessageNotReadableException ex
    ) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage() + " (Input data invalid)",
                ErrorCode.INVALID_PARAM
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());

//        increaseClientError(req, ex);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleNumberFormatException(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                CommonResponseMessages.FORMAT_VALUE_INVALID
                        + ". Parameter: " + ex.getName() +
                        (ex.getValue() == null ? "" : ", value: " + ex.getValue()),
                ErrorCode.INVALID_PARAM
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());

//        increaseClientError(req, ex);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException manve) {
        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

        if (fieldErrors != null && fieldErrors.size() > 0) {
            System.out.println(fieldErrors.get(0).getField());
            ErrorResponse errorResponse = new ErrorResponse(
                    fieldErrors.get(0).getField() + " is required",
                    ErrorCode.INVALID_PARAM
            );
            errorResponse.setTime(LocalDateTime.now());
            errorResponse.setPath(request.getServletPath());
            errorResponse.setHttpMethod(request.getMethod());

            return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
        }
        ErrorResponse errorResponse = new ErrorResponse(
                "Error param",
                ErrorCode.INVALID_PARAM
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(request.getServletPath());
        errorResponse.setHttpMethod(request.getMethod());

//        increaseClientError(request, manve);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ErrorCode.INVALID_PARAM
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(request.getServletPath());
        errorResponse.setHttpMethod(request.getMethod());

//        increaseClientError(request, ex);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }
    //endregion

    //region Server errors
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity httpDataIntegrityViolationException(
            HttpServletRequest request, DataIntegrityViolationException ex
    ) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ErrorCode.UNCATEGORIZED_SERVER_ERROR
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(request.getServletPath());
        errorResponse.setHttpMethod(request.getMethod());

        logApiError(request, ex);

        return new ResponseEntity<>(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity httpServerErrorException(
            HttpServletRequest req, HttpStatusCodeException ex
    ) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ErrorCode.EXTERNAL_SERVICE_FAIL
        );
        String response = ex.getResponseBodyAsString();
        if (StringUtils.isNotBlank(response)) {
            TypeReference<HashMap<String, Object>> typeRef
                    = new TypeReference<HashMap<String, Object>>() {};
            try {
                HashMap<String, Object> mapResponse = objectMapper.readValue(response, typeRef);
                if (mapResponse.containsKey("message")) {
                    Object message = mapResponse.get("message");
                    if (message instanceof String) {
                        errorResponse.setMessage((String)message);
                    }
                } else if (mapResponse.containsKey("msg")) {
                    Object message = mapResponse.get("msg");
                    if (message instanceof String) {
                        errorResponse.setMessage((String)message);
                    }
                }
            } catch (Exception e) {}
        }
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());


        logApiError(req, ex);

        return new ResponseEntity<>(errorResponse, errorResponse.getErrorCode().getHttpStatus());

    }
    //endregion

    //custom defined client error
    @ExceptionHandler(ExceptionResponse.class)
    public ResponseEntity handleExceptionResponse(HttpServletRequest req, ExceptionResponse ex) {
//        ex.printStackTrace();

        //temp support old error format for backward compatible
        if (ex.getErrorObject() != null) {
            ex.getErrorObject().setTime(LocalDateTime.now());
            ex.getErrorObject().setPath(req.getServletPath());
            ex.getErrorObject().setHttpMethod(req.getMethod());

//            increaseClientError(req, ex);

            try {
                LOG.warn(objectMapper.writeValueAsString(ex.getErrorObject()));
                if (ex.getErrorObject().getErrorCode() == ObjectError.ERROR_NOT_FOUND) {
                    LOG.warn(req.getHeader("user-agent"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>(ex.getErrorObject(), ex.getStatus());
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());
        errorResponse.setMessage(ex.getMessage());

        if (ex.getErrorCode() != null) {
            errorResponse.setErrorCode(ex.getErrorCode());
        } else {
            errorResponse.setErrorCode(ErrorCode.UNCATEGORIZED_CLIENT_ERROR);
        }

//        increaseClientError(req, ex);

        try {
            LOG.warn(objectMapper.writeValueAsString(errorResponse));
            if (ErrorCode.RESOURCE_NOT_FOUND.equals(ex.getErrorCode())) {
                LOG.warn(req.getHeader("user-agent"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(errorResponse, ex.getHttpStatus());
    }

    //all other server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(HttpServletRequest req, Exception ex) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ErrorCode.UNCATEGORIZED_SERVER_ERROR
        );
        errorResponse.setTime(LocalDateTime.now());
        errorResponse.setPath(req.getServletPath());
        errorResponse.setHttpMethod(req.getMethod());

        logApiError(req, ex);

        return new ResponseEntity(errorResponse, errorResponse.getErrorCode().getHttpStatus());
    }

    private void logApiError(HttpServletRequest request, Exception e) {
        try {
//            increaseServerError();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (commonConfig.getLogApiError() == null || !commonConfig.getLogApiError()) {
            return;
        }

        if (e instanceof ClientAbortException) {
            //not server fault
            return;
        }

        try {
            String query = request.getQueryString();
            String body = getRequestBody(request);

            LogApiError logApiError = new LogApiError();
            logApiError.setService(commonConfig.getService());
            logApiError.setPath(request.getServletPath());
            logApiError.setExceptionName(e.getClass().getSimpleName());
            logApiError.setStacktrace(ExceptionUtils.getStackTrace(e));
            logApiError.setQuery(query);
            logApiError.setBody(body);
            logApiError.setHttpMethod(request.getMethod());
            logApiError.setPage(request.getHeader("referer"));

            logApiErrorRepository.save(logApiError);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        String body = null;
        try {
            ServletInputStream in = request.getInputStream();
            if (in instanceof CacheServletInputStream) {
                CacheServletInputStream cache = (CacheServletInputStream) in;
                body = cache.getContent();
            } else if (request instanceof ContentCachingRequestWrapper) {
                body = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray());
            } else {
                LOG.error("Not a supported http request. Can not log body request");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

}
