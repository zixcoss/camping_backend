package com.camp.camping_service.utils;

import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CalBooking {

    public static TotalResult calTotal(Instant checkIn, Instant checkOut, long price){
        if(checkIn == null || checkOut == null){
            return null;
        }
        int totalNight = Math.toIntExact(calNight(checkIn, checkOut));
        long total = totalNight * price;
        return TotalResult.builder()
                .total(total)
                .totalNight(totalNight)
                .build();
    }

    private static long calNight(Instant checkIn, Instant checkOut){
        return ChronoUnit.DAYS.between(checkIn,checkOut);
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class TotalResult{
        private long total;
        private int totalNight;
    }
}
