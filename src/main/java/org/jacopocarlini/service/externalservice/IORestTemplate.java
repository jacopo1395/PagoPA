package org.jacopocarlini.service.externalservice;

import org.jacopocarlini.constants.AppConstants;
import org.jacopocarlini.exception.IOCallException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IORestTemplate extends RestTemplate {

    public static final String INVALID_IO_RESPONSE = "Invalid IO response: ";

    @Value(value = "${io_api_key}")
    private String apiKey;


    /**
     * Add authentication header for IO API before submit a request and check constraints of the response
     *
     * @param url              of the request
     * @param method           http method of the request
     * @param requestEntity    body of the request if needed
     * @param responseType     class of the response
     * @param additionalHeader headers of the request
     * @param uriVariables     uri variables of the {@code url}
     * @param <T>              class of response
     * @param <V>              class of request
     * @return the response mapped in {@code responseType} class
     * @throws IOCallException if the response violates the constraints
     */
    public <T, V> T exchangeAuthenticated(String url,
                                          HttpMethod method,
                                          V requestEntity,
                                          Class<T> responseType,
                                          HttpHeaders additionalHeader,
                                          Object... uriVariables) {

        HttpHeaders headers = addAuthenticationHeader(additionalHeader);
        HttpEntity<V> request = new HttpEntity<>(requestEntity, headers);

        var result = exchange(url, method, request, responseType, uriVariables).getBody();
        validateResponse(result);

        return result;
    }

    private <T> void validateResponse(T result) {
        var factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(result);
        if (!violations.isEmpty()) {
            String details = violations.stream()
                    .map(elem -> elem.getPropertyPath() + " " + elem.getMessage())
                    .collect(Collectors.joining(", "));
            throw new IOCallException(INVALID_IO_RESPONSE + details, HttpStatus.BAD_GATEWAY);
        }
    }

    private HttpHeaders addAuthenticationHeader(HttpHeaders additionalHeader) {
        if (additionalHeader == null) {
            additionalHeader = new HttpHeaders();
        }
        additionalHeader.add(AppConstants.IOUrl.OCP_APIM_SUBSCRIPTION_KEY, apiKey);
        return additionalHeader;
    }


}
