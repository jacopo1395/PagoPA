package org.jacopocarlini.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jacopocarlini.constants.AppConstants;
import org.jacopocarlini.model.ErrorResponse;
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
@Tag(name = "Message", description = "the Message API")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * @param submitMessageRequest request to send a message to a user
     * @return a response with the message id if success
     */
    @Operation(summary = "Send a message to a user", description = "Send a custom message to a specific user identified by fiscal code", tags = {"Message"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping(path = AppConstants.Url.SUBMIT_MESSAGE_FOR_USER, consumes = "application/json", produces = "application/json")
    public ResponseEntity<MessageResponse> submitMessageForUser(
            @Parameter(description = "Message and user", required = true)
            @RequestBody() @Valid @NotNull SubmitMessageRequest submitMessageRequest) {
        MessageResponse body = messageService.submitMessageForUser(submitMessageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

}
