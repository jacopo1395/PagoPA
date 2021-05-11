package org.jacopocarlini.exception;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String SERVER_ERROR = "Internal server error";
    private static final String ERROR_IO_CALL = "Error during IO call";
    private static final String APPLICATION_ERROR_OCCURRED = "Application error occurred";
    private static final String INVALID_INPUT = "Invalid Input";

    /**
     * Handle if the input request is not a valid JSON
     *
     * @param ex      {@link HttpMessageNotReadableException} exception raised
     * @param headers of the response
     * @param status  of the response
     * @param request from frontend
     * @return a {@link ErrorResponse} as response with the cause and with a 400 as HTTP status
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(INVALID_INPUT)
                .details(ex.getCause().getCause().getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle if validation constraints are unsatisfied
     *
     * @param ex      {@link MethodArgumentNotValidException} exception raised
     * @param headers of the response
     * @param status  of the response
     * @param request from frontend
     * @return a {@link ErrorResponse} as response with the cause and with a 400 as HTTP status
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        var detailsMessage = String.join(", ", details);
        log.warn(detailsMessage);
        var errorResponse = ErrorResponse.builder()
                .cause(INVALID_INPUT)
                .details(detailsMessage)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle if a {@link IOCallException} is raised
     *
     * @param ex      {@link IOCallException} exception raised
     * @param request from frontend
     * @return a {@link ErrorResponse} as response with the cause and with an appropriated HTTP status
     */
    @ExceptionHandler({IOCallException.class})
    public ResponseEntity<ErrorResponse> handleIOException(final IOCallException ex, final WebRequest request) {
        log.warn(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(ERROR_IO_CALL)
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle if a {@link AppException} is raised
     *
     * @param ex      {@link AppException} exception raised
     * @param request from frontend
     * @return a {@link ErrorResponse} as response with the cause and with an appropriated HTTP status
     */
    @ExceptionHandler({AppException.class})
    public ResponseEntity<ErrorResponse> handleAppException(final AppException ex, final WebRequest request) {
        log.warn(ex.toString());
        var errorResponse = ErrorResponse.builder()
                .cause(APPLICATION_ERROR_OCCURRED)
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle if a {@link Exception} is raised
     *
     * @param ex      {@link Exception} exception raised
     * @param request from frontend
     * @return a {@link ErrorResponse} as response with the cause and with 500 as HTTP status
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex, final WebRequest request) {
        log.error(ex.toString());
        ex.printStackTrace();
        var errorResponse = ErrorResponse.builder()
                .cause(SERVER_ERROR)
                .details(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
