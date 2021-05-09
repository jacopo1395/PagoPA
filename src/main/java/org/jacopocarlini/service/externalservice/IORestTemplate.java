package org.jacopocarlini.service.externalservice;

import org.jacopocarlini.constants.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IORestTemplate extends RestTemplate {

    @Value(value = "${io_api_key}")
    private String apiKey;


    /**
     * Add authentication header for IO API before submit a request
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
     */
    public <T, V> T exchangeAuthenticated(String url,
                                          HttpMethod method,
                                          V requestEntity,
                                          Class<T> responseType,
                                          HttpHeaders additionalHeader,
                                          Object... uriVariables) {

        HttpHeaders headers = addAuthenticationHeader(additionalHeader);
        HttpEntity<V> request = new HttpEntity<>(requestEntity, headers);
        return exchange(url, method, request, responseType, uriVariables).getBody();
    }

    private HttpHeaders addAuthenticationHeader(HttpHeaders additionalHeader) {
        if (additionalHeader == null) {
            additionalHeader = new HttpHeaders();
        }
        additionalHeader.add(AppConstants.IOUrl.OCP_APIM_SUBSCRIPTION_KEY, apiKey);
        return additionalHeader;
    }


}
