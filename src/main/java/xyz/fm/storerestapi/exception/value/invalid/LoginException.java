package xyz.fm.storerestapi.exception.value.invalid;

import xyz.fm.storerestapi.error.ErrorCode;

public class LoginException extends InvalidValueException {

    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
