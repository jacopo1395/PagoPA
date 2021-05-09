package org.jacopocarlini.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IOError {

    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
}
