package org.jacopocarlini.model.io;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MessageRequest {

    @JsonProperty(value = "time_to_live")
    private Integer timeToLive;

    @JsonProperty(value = "content")
    private Content content;

    @JsonProperty(value = "default_addresses")
    private Address defaultAddresses;

    @JsonProperty(value = "fiscal_code")
    private String fiscalCode;

}
