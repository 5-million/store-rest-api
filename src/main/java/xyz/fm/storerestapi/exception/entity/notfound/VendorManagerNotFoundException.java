package xyz.fm.storerestapi.exception.entity.notfound;

import xyz.fm.storerestapi.error.ErrorCode;

public class VendorManagerNotFoundException extends EntityNotFoundException {

    public VendorManagerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
