package org.jacopocarlini.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Content {

    @JsonProperty(value = "subject")
    private String subject;

    @JsonProperty(value = "markdown")
    private String markdown;

    @JsonProperty(value = "due_date")
    private LocalDateTime dueDate;

    @JsonProperty(value = "payment_data")
    private PaymentData paymentData;

    @JsonProperty(value = "prescription_data")
    private PrescriptionData prescriptionData;


}
