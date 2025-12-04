package com.camp.camping_service.dto.select;

import ch.qos.logback.core.util.StringUtil;

import java.math.BigDecimal;

public record SelectLandmarkListRecord(
        String code,
        String title,
        String description,
        Long price,
        BigDecimal lat,
        BigDecimal lng,
        String imageUrl,
        String favoriteId
) {
    public boolean isFavorite(){
        return !StringUtil.isNullOrEmpty(favoriteId);
    }
}
