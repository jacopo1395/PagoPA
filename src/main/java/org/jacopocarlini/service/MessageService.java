package org.jacopocarlini.service;

import org.jacopocarlini.model.SubmitMessageRequest;
import org.jacopocarlini.model.io.MessageResponse;

public interface MessageService {


    MessageResponse submitMessageForUser(SubmitMessageRequest submitMessageRequest);
}
