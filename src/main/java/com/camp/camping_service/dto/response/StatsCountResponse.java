package com.camp.camping_service.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsCountResponse {
    private long usersCount;
    private long campingCount;
    private long bookingCount;
}
