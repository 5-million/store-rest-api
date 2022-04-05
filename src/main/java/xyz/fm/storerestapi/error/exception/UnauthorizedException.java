package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(Error error) {
        super(error);
    }

    public UnauthorizedException(Error error, String detail) {
        super(error, detail);
    }

    public UnauthorizedException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
