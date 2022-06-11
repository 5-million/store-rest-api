package xyz.fm.storerestapi.exception.entity.duplicate;

import xyz.fm.storerestapi.error.ErrorCode;

public class DuplicateCategoryException extends DuplicateEntityException {

    public DuplicateCategoryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
