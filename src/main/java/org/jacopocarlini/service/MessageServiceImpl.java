package org.jacopocarlini.service;

import lombok.extern.slf4j.Slf4j;
import org.jacopocarlini.exception.AppException;
import org.jacopocarlini.model.SubmitMessageRequest;
import org.jacopocarlini.model.io.MessageResponse;
import org.jacopocarlini.model.io.ProfileResponse;
import org.jacopocarlini.service.externalservice.IOClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    public static final String MESSAGE_SENT = "Message sent to user";
    public static final String MESSAGES_NOT_ALLOWED = "The user does not allow to receive messages";

    @Autowired
    private IOClient ioClient;

    /**
     * @param request the message to send and the user that receive the message
     * @return the message id if the profile exists and if user allowed to receive message otherwise throws an {@link AppException}
     */
    @Override
    public MessageResponse submitMessageForUser(SubmitMessageRequest request) {
        ProfileResponse profile = ioClient.getProfile(request.getFiscalCode());
        if (profile != null && profile.getSenderAllowed()) {
            MessageResponse response = ioClient.submitMessageForUser(request.getFiscalCode(), request.getMessage());
            log.info(MESSAGE_SENT);
            return response;
        }
        throw new AppException(MESSAGES_NOT_ALLOWED, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
