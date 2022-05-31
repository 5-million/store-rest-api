package xyz.fm.storerestapi.error;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {

    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "common-001", "Internal server error."),
    INVALID_ARGUMENT(BAD_REQUEST, "common-002", "invalid arguments included."),


    // jwt
    INVALID_SIGN_JWT(UNAUTHORIZED, "jwt-001", "invalid signature json web token."),
    EXPIRED_JWT(UNAUTHORIZED, "jwt-002", "expired json web token."),
    UNSUPPORTED_JWT(UNAUTHORIZED, "jwt-003", "unsupported json web token."),
    INVALID_JWT(UNAUTHORIZED, "jwt-004", "invalid json web token."),
    UNREGISTERED_JWT(NOT_FOUND, "jwt-005", "unregistered json web token."),
    JWT_UNAUTHORIZED(UNAUTHORIZED, "jwt-003", "unauthorized"),
    JWT_NO_PERMISSION(FORBIDDEN, "jwt-004", "no permission."),

    // user
    DUPLICATE_EMAIL(CONFLICT, "user-001", "already registered email."),
    DUPLICATE_PHONE(CONFLICT, "user-002", "already registered phone number."),
    PWD_NOT_EQUAL_TO_CONFIRM_PWD(BAD_REQUEST, "user-003", "password not equal to confirm password."),
    USER_NOT_FOUND(NOT_FOUND, "user-004", "unregistered user."),
    LOGIN_FAIL(BAD_REQUEST, "user-005", "please check your email and password."),

    // consumer
    DUPLICATE_CONSUMER(CONFLICT, "consumer-001", "already registered consumer."),

    // vendor
    DUPLICATE_VENDOR_NAME(CONFLICT, "vendor-001", "already registered vendor name."),
    DUPLICATE_VENDOR_REG_NUMBER(CONFLICT, "vendor-002", "already registered vendor regNumber."),
    VENDOR_NOT_FOUND(NOT_FOUND, "vendor-003", "unregistered vendor."),

    // vendor manager
    VENDOR_MANAGER_NOT_FOUND(NOT_FOUND, "vm-001", "unregistered vendor manager."),
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
