package com.camp.camping_service.dto.response;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationListResponse{
    private String bookingCode;
    private Instant checkIn;
    private Instant checkOut;
    private Long total;
    private Integer totalNight;
    private Boolean paymentStatus;
    private String landmarkCode;
    private String landmarkTitle;
}
