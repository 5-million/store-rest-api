package xyz.fm.storerestapi.exception.entity.duplicate;

import xyz.fm.storerestapi.error.ErrorCode;

public class DuplicateVendorManagerException extends DuplicateEntityException {

    public DuplicateVendorManagerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
