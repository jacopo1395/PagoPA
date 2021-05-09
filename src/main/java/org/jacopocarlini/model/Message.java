package org.jacopocarlini.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class Message {

    @NotNull
    @Size(min = 10, max = 120)
    String subject;

    @NotNull
    @Size(min = 80, max = 10000)
    String markdown;
}
