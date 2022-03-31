package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class DuplicationException extends CustomException {

    public DuplicationException(Error error) {
        super(error);
    }

    public DuplicationException(Error error, String detail) {
        super(error, detail);
    }

    public DuplicationException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
