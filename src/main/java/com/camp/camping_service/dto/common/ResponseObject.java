package com.camp.camping_service.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseObject {
    private boolean status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ErrorObject> errors;
}
