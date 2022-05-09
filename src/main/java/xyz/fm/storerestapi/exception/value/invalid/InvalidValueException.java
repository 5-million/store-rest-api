package xyz.fm.storerestapi.exception.value.invalid;

import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;

public class InvalidValueException extends CustomException {

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
