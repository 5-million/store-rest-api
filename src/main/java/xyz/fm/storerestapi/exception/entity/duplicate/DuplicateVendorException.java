package xyz.fm.storerestapi.exception.entity.duplicate;

import xyz.fm.storerestapi.error.ErrorCode;

public class DuplicateVendorException extends DuplicateEntityException {

    public DuplicateVendorException(ErrorCode errorCode) {
        super(errorCode);
    }
}
