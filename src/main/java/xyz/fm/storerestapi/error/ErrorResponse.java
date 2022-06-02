package xyz.fm.storerestapi.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;

@Getter
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

    public void setRegisteredEmail(String registeredEmail) {
        this.registeredEmail = registeredEmail;
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String detail;

        public static ValidationError of(FieldError fieldError) {
            return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        public static ValidationError of(ConstraintViolation<?> cv) {
            return new ErrorResponse.ValidationError(cv.getPropertyPath().toString(), cv.getMessage());
        }
    }
}
