package com.camp.camping_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CampingResponse {
    private String code;
    private String title;
    private String description;
    private BigDecimal lat;
    private BigDecimal lng;
    private String imageUrl;
    private Long price;
    private String category;
    private Boolean isFavorite;
}
