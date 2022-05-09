package xyz.fm.storerestapi.error;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {

    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "common-001", "Internal server error."),
    INVALID_ARGUMENT(BAD_REQUEST, "common-002", "invalid arguments included."),

    // user
    DUPLICATE_EMAIL(CONFLICT, "user-001", "already registered email."),
    DUPLICATE_PHONE(CONFLICT, "user-002", "already registered phone number."),
    PWD_NOT_EQUAL_TO_CONFIRM_PWD(BAD_REQUEST, "user-003", "password not equal to confirm password."),

    // consumer
    DUPLICATE_CONSUMER(CONFLICT, "consumer-001", "already registered consumer."),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
