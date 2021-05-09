package org.jacopocarlini.exception;

import lombok.extern.java.Log;
import org.jacopocarlini.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Log
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String SERVER_ERROR = "Internal server error";
    private static final String ERROR_IO_CALL = "Error during IO call";
    private static final String APPLICATION_ERROR_OCCURRED = "Application error occurred";
    private static final String INVALID_INPUT = "Invalid Input";

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warning(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(INVALID_INPUT)
                .details(ex.getCause().getCause().getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        var detailsMessage = String.join(", ", details);
        log.warning(detailsMessage);
        var errorResponse = ErrorResponse.builder()
                .cause(INVALID_INPUT)
                .details(detailsMessage)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({IOCallException.class})
    public ResponseEntity<ErrorResponse> handleIOException(final IOCallException ex, final WebRequest request) {
        log.warning(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(ERROR_IO_CALL)
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler({AppException.class})
    public ResponseEntity<ErrorResponse> handleAppException(final AppException ex, final WebRequest request) {
        log.warning(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(APPLICATION_ERROR_OCCURRED)
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex, final WebRequest request) {
        log.severe(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(ex.getMessage())
                .details(SERVER_ERROR)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
