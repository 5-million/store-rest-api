package xyz.fm.storerestapi.exception.entity.notfound;

import xyz.fm.storerestapi.error.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
