package com.camp.camping_service.dto.response;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyCampingResponse {
    private String code;
    private String title;
    private Long price;
    private Instant createAt;
    private Instant updateAt;
}
