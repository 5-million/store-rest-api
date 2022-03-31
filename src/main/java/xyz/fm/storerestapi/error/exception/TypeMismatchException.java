package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class TypeMismatchException extends CustomException {

    public TypeMismatchException(Error error) {
        super(error);
    }

    public TypeMismatchException(Error error, String detail) {
        super(error, detail);
    }

    public TypeMismatchException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
