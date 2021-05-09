package org.jacopocarlini.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Value
@NotNull
public class SubmitMessageRequest {

    @Schema(description = "Message to send", required = true)
    @NotNull
    @Valid
    Message message;

    /**
     * User's fiscal code
     */

    @Schema(description = "User's fiscal code", required = true)
    @Pattern(regexp = "[A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]")
    @NotNull
    String fiscalCode;
}
