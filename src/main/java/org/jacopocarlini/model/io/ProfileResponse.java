package org.jacopocarlini.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class ProfileResponse {

    @JsonProperty(value = "sender_allowed")
    private Boolean senderAllowed;

    @JsonProperty(value = "preferred_languages")
    private List<String> preferredLanguages;
}
