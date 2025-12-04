package com.camp.camping_service.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingListResponse {
    private String bookingCode;
    private Instant checkIn;
    private Instant checkOut;
    private Long total;
    private Integer totalNight;
    private Boolean paymentStatus;
    private String landmarkCode;
    private String landmarkTitle;
}
