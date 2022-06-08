package xyz.fm.storerestapi.exception.entity.notfound;

import xyz.fm.storerestapi.error.ErrorCode;

public class CategoryNotFoundException extends EntityNotFoundException {

    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
