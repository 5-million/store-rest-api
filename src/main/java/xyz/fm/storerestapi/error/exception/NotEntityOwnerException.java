package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class NotEntityOwnerException extends CustomException {

    public NotEntityOwnerException(Error error) {
        super(error);
    }

    public NotEntityOwnerException(Error error, String detail) {
        super(error, detail);
    }

    public NotEntityOwnerException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
