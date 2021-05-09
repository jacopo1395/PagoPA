package org.jacopocarlini.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Value
public class IOCallException extends RuntimeException {

    /**
     * message to return in the response when this exception occurred
     */
    String details;
    /**
     * http status to return in the response when this exception occurred
     */
    HttpStatus httpStatus;


    public IOCallException(String details, HttpStatus httpStatus) {
        this.details = details;
        this.httpStatus = httpStatus;
    }
}
