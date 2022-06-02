package xyz.fm.storerestapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.error.ErrorResponse;
import xyz.fm.storerestapi.exception.CustomException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, List<ErrorResponse.ValidationError> fieldErrors) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode, fieldErrors));
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePhoneException(DuplicatePhoneException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        errorResponse.setRegisteredEmail(e.getRegisteredEmail());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorResponse.ValidationError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return buildErrorResponse(ErrorCode.INVALID_ARGUMENT, fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<ErrorResponse.ValidationError> fieldErrors = constraintViolations
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return buildErrorResponse(ErrorCode.INVALID_ARGUMENT, fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(errorCode);
    }
}
