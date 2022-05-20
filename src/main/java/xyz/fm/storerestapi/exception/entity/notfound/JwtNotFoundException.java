package xyz.fm.storerestapi.exception.entity.notfound;

import xyz.fm.storerestapi.error.ErrorCode;

public class JwtNotFoundException extends EntityNotFoundException{

    public JwtNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
