package xyz.fm.storerestapi.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ErrorResponse {

    private String error;
    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    public static ErrorResponse of(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.error = errorCode.getCode();
        errorResponse.message = errorCode.getMessage();
        return errorResponse;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class ValidationError {
        private final String field;
        private final String detail;

        public ValidationError(String field, String detail) {
            this.field = field;
            this.detail = detail;
        }

        public String getField() {
            return field;
        }

        public String getDetail() {
            return detail;
        }
    }
}
