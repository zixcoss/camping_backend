package com.camp.camping_service.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {
    private long campingCount;
    private long nights;
    private long totals;
}
