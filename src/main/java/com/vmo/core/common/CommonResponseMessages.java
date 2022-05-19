package com.vmo.core.common;

public interface CommonResponseMessages {
    //performance filter
    String SERVICE_TEMPORARY_UNAVAILABLE = "Service temporary unavailable";

    //authentication
    String MUST_LOGIN = "You must login to access this function";
    String INVALID_TOKEN = "Invalid JWT token";
    String INVALID_API_KEY = "The API key was not found or not the expected value";


    //pageable
    String PAGE_NUMBER_NOT_NOT_NEGATIVE = "Page index must be equal or higher than 0";
    String PAGE_SIZE_MUST_POSITIVE = "Page size must be positive";


    //general error
    String FORMAT_VALUE_INVALID = "Invalid format value";


    //job
    String SAFE_SHUTDOWN_SUCCESS = "All jobs done. Safe to shutdown now";
    String DEQUEUE_JOB_FAIL = "Something went wrong, could not dequeue job";
    String DUPLICATE_JOB = "Queue same jobs or jobs having same name are not allowed";
    String JOB_CONFIG_NOT_EXIST = "Job configuration does not exist";
    String JOB_CONFIG_OUTDATED = "Unknown job configuration";
    String SECOND_INVALID = "Invalid second value";
    String MINUTE_INVALID = "Invalid minute value";
    String HOUR_INVALID = "Invalid hour value";
    String DAY_OF_WEEK_INVALID = "Invalid day of week value";
    String DAY_OF_MONTH_INVALID = "Invalid day of month value";
    String MONTH_INVALID = "Invalid month value";
    String CRON_EXPRESSION_NOT_EMPTY = "Cron expression must not empty";
    String NO_VALID_CRON = "Either fullCronString or buildCronSupport must be provided";
    String JOB_TIME_SECOND_INVALID = "Job time seconds must >= 60";
    String JOB_RUNNING_SAME_TIME = "Duplicate running job at same time";

    String SUCCESS = "Success.";
    String CONNECTION_LOSS = "Loss of connection to database.";
}
