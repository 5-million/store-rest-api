package xyz.fm.storerestapi.error;

public enum Error {

    NOT_VALID("valid-001", "요청한 데이터가 유효하지 않습니다."),
    TYPE_MISMATCH("valid-002", "지원하지 않는 타입입니다."),
    DUPLICATE("conflict-001", "이미 존재합니다."),
    ;

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
