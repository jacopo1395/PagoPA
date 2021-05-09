package org.jacopocarlini.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Value
@NotNull
public class SubmitMessageRequest {

    @NotNull
    @Valid
    Message message;

    @NotNull
    @Size(min = 16, max = 16)
    String fiscalCode;
}
