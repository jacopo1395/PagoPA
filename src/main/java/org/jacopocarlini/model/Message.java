package org.jacopocarlini.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class Message {

    /**
     * subject of the message
     */
    @Schema(description = "Subject of the message", required = true)
    @NotNull
    @Size(min = 10, max = 120)
    String subject;

    /**
     * text in markdown of the message
     */
    @Schema(description = "Text of the message in markdown", required = true)
    @NotNull
    @Size(min = 80, max = 10000)
    String markdown;
}
