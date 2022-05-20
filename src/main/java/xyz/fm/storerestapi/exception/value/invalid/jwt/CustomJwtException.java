package xyz.fm.storerestapi.exception.value.invalid.jwt;

import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.InvalidValueException;

public class CustomJwtException extends InvalidValueException {

    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
