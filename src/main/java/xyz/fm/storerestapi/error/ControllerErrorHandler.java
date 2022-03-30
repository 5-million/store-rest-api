package xyz.fm.storerestapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorHandler {

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Error error) {
        return buildResponse(status, error, null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Error error, String detail) {
        return ResponseEntity.status(status).body(new ErrorResponse(error, detail));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadValidException(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = Error.INVALID_FORMAT;
        String detail = "";

        if (exception instanceof MethodArgumentNotValidException) {
            detail = ((MethodArgumentNotValidException) exception).getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .getDefaultMessage();
        }

        return buildResponse(status, error, detail);
    }
}
