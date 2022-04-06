package xyz.fm.storerestapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.fm.storerestapi.error.exception.CustomException;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

@RestControllerAdvice
public class ControllerErrorHandler {

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Error error) {
        return buildResponse(status, error, null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Error error, String detail) {
        return ResponseEntity.status(status).body(new ErrorResponse(error, detail));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = exception.getError();
        String detail = exception.isDetailIgnoreForUser() ? "" : exception.getDetail();

        if (exception instanceof DuplicationException) {
            status = HttpStatus.CONFLICT;
        } else if (exception instanceof UnauthorizedException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }

        return buildResponse(status, error, detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadValidException(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = Error.NOT_VALID;
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
