package org.jacopocarlini.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PaymentData {

    @JsonProperty(value = "amount")
    private Integer amount;

    @JsonProperty(value = "notice_number")
    private String noticeNumber;

    @JsonProperty(value = "invalid_after_due_date")
    private Boolean invalidAfterDueDate;

}
