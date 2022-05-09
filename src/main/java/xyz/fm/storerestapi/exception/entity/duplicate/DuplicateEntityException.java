package xyz.fm.storerestapi.exception.entity.duplicate;

import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;

public class DuplicateEntityException extends CustomException {

    public DuplicateEntityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
