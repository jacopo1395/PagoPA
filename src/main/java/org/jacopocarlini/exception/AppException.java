package org.jacopocarlini.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Value
public class AppException extends RuntimeException {

    String details;
    HttpStatus httpStatus;

    public AppException(String details, HttpStatus httpStatus) {
        this.details = details;
        this.httpStatus = httpStatus;
    }
}
