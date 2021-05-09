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
public class PrescriptionData {

    @JsonProperty(value = "nre")
    private String numeroRicettaElettronica;

    @JsonProperty(value = "iup")
    private String  identificativoUnicoPrescrizione;

    @JsonProperty(value = "prescriber_fiscal_code")
    private String prescriberFiscalCode;

}
