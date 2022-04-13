package xyz.fm.storerestapi.error;

public enum Error {

    NOT_VALID("valid-001", "요청한 데이터가 유효하지 않습니다."),
    TYPE_MISMATCH("valid-002", "지원하지 않는 타입입니다."),
    INVALID_PASSWORD("valid-003", "유효하지 않는 비밀번호입니다."),
    DUPLICATE("conflict-001", "이미 존재합니다."),
    LOGIN_FAIL("login-fail", "등록되지 않은 이메일이거나, 이메일 또는 비밀번호를 잘못 입력하셨습니다."),
    NOT_FOUND("found-001", "찾을 수 없습니다."),
    UNAUTHORIZED("auth-001", "인증정보가 올바르지 않습니다."),
    NO_PERMISSION("perm-001", "권한이 없습니다."),
    NOT_APPROVED("login-fail", "등록이 승인되지 않았습니다. 관리자에게 문의하세요."),
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
