package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class LoginException extends CustomException {

    public LoginException(Error error) {
        super(error);
    }

    public LoginException(Error error, String detail) {
        super(error, detail);
    }

    public LoginException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
