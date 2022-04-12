package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class NoPermissionException extends CustomException {

    public NoPermissionException(Error error) {
        super(error);
    }

    public NoPermissionException(Error error, String detail) {
        super(error, detail);
    }

    public NoPermissionException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
