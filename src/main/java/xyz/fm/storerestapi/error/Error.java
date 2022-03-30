package xyz.fm.storerestapi.error;

public enum Error {

    INVALID_FORMAT("valid-001", "request data in invalid format"),;

    private String code;
    private String message;

    Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
