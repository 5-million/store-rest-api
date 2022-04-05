package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class NotFoundException extends CustomException {

    public NotFoundException(Error error) {
        super(error);
    }

    public NotFoundException(Error error, String detail) {
        super(error, detail);
    }

    public NotFoundException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
