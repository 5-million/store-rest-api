package xyz.fm.storerestapi.exception.entity.nopermission;

import xyz.fm.storerestapi.error.ErrorCode;

public class VendorManagerAuthorityException extends NoPermissionException {

    public VendorManagerAuthorityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
