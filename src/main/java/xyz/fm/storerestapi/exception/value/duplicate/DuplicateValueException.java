package xyz.fm.storerestapi.exception.value.duplicate;

import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;

public class DuplicateValueException extends CustomException {

    public DuplicateValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
