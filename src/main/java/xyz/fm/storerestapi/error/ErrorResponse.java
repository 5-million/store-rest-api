package xyz.fm.storerestapi.error;

public class ErrorResponse {

    private final String error;
    private final String message;
    private String detail;

    public ErrorResponse(Error error) {
        this(error, "");
    }

    public ErrorResponse(Error error, String detail) {
        this.error = error.getCode();
        this.message = error.getMessage();
        this.detail = detail;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
