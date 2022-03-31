package xyz.fm.storerestapi.error.exception;

import xyz.fm.storerestapi.error.Error;

public class CustomException extends RuntimeException {

    private final Error error;
    private String detail = "";
    private boolean detailIgnoreForUser = false;

    public CustomException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public CustomException(Error error, String detail) {
        super(error.getMessage());
        this.error = error;
        this.detail = detail;
    }

    public CustomException(Error error, String detail, boolean detailIgnoreForUser) {
        super(error.getMessage());
        this.error = error;
        this.detail = detail;
        this.detailIgnoreForUser = detailIgnoreForUser;
    }

    public Error getError() {
        return error;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isDetailIgnoreForUser() {
        return detailIgnoreForUser;
    }
}
