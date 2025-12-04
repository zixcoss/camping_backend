package com.camp.camping_service.dto.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorObject {
    private String code;
    private String message;
}
