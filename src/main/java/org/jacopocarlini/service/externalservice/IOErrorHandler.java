package org.jacopocarlini.service.externalservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jacopocarlini.exception.IOCallException;
import org.jacopocarlini.model.io.IOError;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
@Slf4j
public class IOErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws java.io.IOException {
        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    /**
     * Handle the error response from IO
     *
     * @param httpResponse response from IO
     * @throws java.io.IOException in case of I/O errors
     * @throws IOCallException     when IO platform responds with an error
     */
    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus statusCode = httpResponse.getStatusCode();
        if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            throw new IOCallException(httpResponse.getStatusText(), statusCode);
        } else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            var ioError = new ObjectMapper().readValue(httpResponse.getBody(), IOError.class);
            log.warn(ioError.toString());
            throw new IOCallException(ioError.getDetail(), statusCode);
        }
    }
}
