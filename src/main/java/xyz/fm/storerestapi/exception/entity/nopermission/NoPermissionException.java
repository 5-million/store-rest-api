package xyz.fm.storerestapi.exception.entity.nopermission;

import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;

public class NoPermissionException extends CustomException {

    public NoPermissionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
