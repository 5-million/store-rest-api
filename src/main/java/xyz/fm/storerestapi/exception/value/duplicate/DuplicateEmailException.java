package xyz.fm.storerestapi.exception.value.duplicate;

import xyz.fm.storerestapi.error.ErrorCode;

public class DuplicateEmailException extends DuplicateValueException{

    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
