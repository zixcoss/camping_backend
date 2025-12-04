package com.camp.camping_service.dto.request;

import lombok.*;

import java.time.Instant;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private String campingCode;
    private Instant checkIn;
    private Instant checkOut;
}
