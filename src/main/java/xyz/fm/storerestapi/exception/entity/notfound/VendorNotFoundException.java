package xyz.fm.storerestapi.exception.entity.notfound;

import xyz.fm.storerestapi.error.ErrorCode;

public class VendorNotFoundException extends EntityNotFoundException {

    public VendorNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
