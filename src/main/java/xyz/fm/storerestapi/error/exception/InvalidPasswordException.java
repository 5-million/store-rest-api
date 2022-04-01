package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException(Error error) {
        super(error);
    }

    public InvalidPasswordException(Error error, String detail) {
        super(error, detail);
    }

    public InvalidPasswordException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
