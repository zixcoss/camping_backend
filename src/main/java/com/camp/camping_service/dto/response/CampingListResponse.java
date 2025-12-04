package com.camp.camping_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampingListResponse {
    private String code;
    private String title;
    private String description;
    private String imageUrl;
    private BigDecimal lat;
    private BigDecimal lng;
    private Long price;
    private Boolean isFavorite;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Center{
        private Double lat;
        private Double lng;
    }
}
