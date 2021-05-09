package org.jacopocarlini.service.externalservice;

import org.jacopocarlini.constants.AppConstants;
import org.jacopocarlini.model.Message;
import org.jacopocarlini.model.io.Content;
import org.jacopocarlini.model.io.MessageRequest;
import org.jacopocarlini.model.io.MessageResponse;
import org.jacopocarlini.model.io.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class IOClient {

    private final IORestTemplate ioRestTemplate;

    @Autowired
    public IOClient(IORestTemplate ioRestTemplate) {
        ioRestTemplate.setErrorHandler(new IOErrorHandler());
        this.ioRestTemplate = ioRestTemplate;
    }

    /**
     * @param fiscalCode User's fiscal code
     * @param message    subject and text in markdown of message
     * @return the message id sent
     */
    public MessageResponse submitMessageForUser(String fiscalCode, Message message) {
        MessageRequest request = MessageRequest.builder()
                .fiscalCode(fiscalCode)
                .content(Content.builder()
                        .subject(message.getSubject())
                        .markdown(message.getMarkdown())
                        .build())
                .build();
        return ioRestTemplate.exchangeAuthenticated(AppConstants.IOUrl.SUBMIT_MESSAGE,
                HttpMethod.POST,
                request,
                MessageResponse.class,
                null);

    }

    /**
     * @param fiscalCode User's fiscal code
     * @return User profile
     */
    public ProfileResponse getProfile(String fiscalCode) {
        return ioRestTemplate.exchangeAuthenticated(AppConstants.IOUrl.GET_PROFILE,
                HttpMethod.GET,
                null,
                ProfileResponse.class,
                null,
                fiscalCode);

    }

}
