package xyz.fm.storerestapi.exception.value.duplicate;

import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.error.ErrorCode;

public class DuplicatePhoneException extends DuplicateValueException {

    private final String registeredEmail;

    public DuplicatePhoneException(ErrorCode errorCode, Email email) {
        super(errorCode);
        registeredEmail = email.encrypt();
    }

    public String getRegisteredEmail() {
        return registeredEmail;
    }
}
