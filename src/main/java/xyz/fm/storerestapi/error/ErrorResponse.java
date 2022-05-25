package xyz.fm.storerestapi.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {

    private String error;
    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String registeredEmail;

    public static ErrorResponse of(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.error = errorCode.getCode();
        errorResponse.message = errorCode.getMessage();
        return errorResponse;
    }

    public static ErrorResponse of(ErrorCode errorCode, List<ValidationError> fieldErrors) {
        ErrorResponse errorResponse = of(errorCode);
        errorResponse.errors = fieldErrors;
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

    public void setRegisteredEmail(String registeredEmail) {
        this.registeredEmail = registeredEmail;
    }

    public String getRegisteredEmail() {
        return registeredEmail;
    }

    public static class ValidationError {
        private final String field;
        private final String detail;

        public ValidationError(String field, String detail) {
//            this.field = field.split("\\.")[0];
            this.field = field;
            this.detail = detail;
        }

        public static ValidationError of(FieldError fieldError) {
            return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        public String getField() {
            return field;
        }

        public String getDetail() {
            return detail;
        }
    }
}
