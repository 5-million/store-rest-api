package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(Error error) {
        super(error);
    }

    public EntityNotFoundException(Error error, String detail) {
        super(error, detail);
    }

    public EntityNotFoundException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error, detail, detailIgnoreForUser);
    }
}
