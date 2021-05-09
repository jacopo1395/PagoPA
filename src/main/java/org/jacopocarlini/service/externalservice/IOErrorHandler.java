package org.jacopocarlini.service.externalservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jacopocarlini.exception.IOCallException;
import org.jacopocarlini.model.io.IOError;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class IOErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws java.io.IOException {
        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws java.io.IOException {
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            throw new IOCallException(httpResponse.getStatusText(), httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            var ioError = new ObjectMapper().readValue(httpResponse.getBody(), IOError.class);
            throw new IOCallException(ioError.getDetail(), httpResponse.getStatusCode());
        }
    }
}
