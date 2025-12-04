package com.camp.camping_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavoriteRequest {
    private String campingCode;
    private Boolean isFavorite;
}
