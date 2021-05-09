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
