package org.jacopocarlini.controller;

import org.jacopocarlini.constants.AppConstants;
import org.jacopocarlini.model.SubmitMessageRequest;
import org.jacopocarlini.model.io.MessageResponse;
import org.jacopocarlini.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;


    @PostMapping(path = AppConstants.Url.SUBMIT_MESSAGE_FOR_USER, consumes = "application/json", produces = "application/json")
    public ResponseEntity<MessageResponse> submitMessageForUser(@RequestBody() @Valid @NotNull SubmitMessageRequest submitMessageRequest) {
        MessageResponse body = messageService.submitMessageForUser(submitMessageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

}
