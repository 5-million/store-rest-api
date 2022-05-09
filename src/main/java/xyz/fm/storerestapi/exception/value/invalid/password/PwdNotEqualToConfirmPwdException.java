package xyz.fm.storerestapi.exception.value.invalid.password;

import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.InvalidValueException;

public class PwdNotEqualToConfirmPwdException extends InvalidValueException {

    public PwdNotEqualToConfirmPwdException(ErrorCode errorCode) {
        super(errorCode);
    }
}
