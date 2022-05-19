package com.vmo.core.modules.models.requests.schedule.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.modules.models.response.ObjectError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.util.List;

public class JobConfigCron implements CommonResponseMessages {
    private String fullCronString;
    @JsonProperty("buildCronSupport")
    private JobConfigCronSupport support;
    @JsonIgnore
    private Boolean noCronBuilder = true;

    public String getFullCronString() {
        return fullCronString;
    }

    public void setFullCronString(String fullCronString) {
        this.fullCronString = fullCronString;
    }

    public JobConfigCronSupport getSupport() {
        return support;
    }

    public void setSupport(JobConfigCronSupport support) {
        this.support = support;
    }

    public void validate() throws ExceptionResponse {
        String error = null;
        noCronBuilder = true;
        if (support == null) {
            //nothing need
        } else if (support.getSeconds() != null && support.getSeconds().size() > 0) {
            if (!notNegativeOrHigherThan(support.getSeconds(), 59)) {
                error = SECOND_INVALID;
            } else {
                noCronBuilder = false;
            }
        } else if (support.getMinutes() != null && support.getMinutes().size() > 0) {
            if (!notNegativeOrHigherThan(support.getMinutes(), 59)) {
                error = MINUTE_INVALID;
            } else {
                noCronBuilder = false;
            }
        } else if (support.getHours() != null && support.getHours().size() > 0) {
            if (!notNegativeOrHigherThan(support.getHours(), 23)) {
                error = HOUR_INVALID;
            } else {
                noCronBuilder = false;
            }
        } else if (support.getDaysOfWeek() != null && support.getDaysOfWeek().size() > 0) {
            if (!notNull(support.getDaysOfWeek())) {
                error = DAY_OF_WEEK_INVALID;
            } else {
                noCronBuilder = false;
            }
        } else if (support.getDaysOfMonth() != null && support.getDaysOfMonth().size() > 0) {
            if (!notNegativeOrHigherThan(support.getDaysOfMonth(), 23)) {
                error = DAY_OF_MONTH_INVALID;
            } else {
                noCronBuilder = false;
            }
        } else if (support.getMonths() != null && support.getMonths().size() > 0) {
            if (!notNegativeOrHigherThan(support.getMonths(), 12)) {
                error = MONTH_INVALID;
            } else {
                noCronBuilder = false;
            }
        }

        if (fullCronString != null) {
            if (StringUtils.isBlank(fullCronString)) {
                error = CRON_EXPRESSION_NOT_EMPTY;
            }
        }

        if (error != null) {
            throw new ExceptionResponse(
                    ObjectError.ERROR_PARAM,
                    error,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @JsonIgnore
    public Boolean getNoCronBuilder() {
        return noCronBuilder;
    }

    private boolean notNegativeOrHigherThan(List<Integer> values, Integer higherThan) throws ExceptionResponse {
        for (Integer number : values) {
            if (!notNull(number) || !notNegativeOrHigherThan(number, higherThan)) {
                return false;
            }
        }
        return true;
    }

    private boolean notNegativeOrHigherThan(Integer value, Integer higherThan) throws ExceptionResponse {
        if (value == null || value < 0 || value > higherThan) {
            return false;
        }
        return true;
    }

    private boolean notNull(Object value) {
        return value != null;
    }

    private boolean notNull(List<DayOfWeek> daysOfWeek) {
        for (DayOfWeek day : daysOfWeek) {
            if (day == null) {
                return false;
            }
        }
        return true;
    }
}
