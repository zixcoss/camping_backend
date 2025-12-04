package com.camp.camping_service.dto.request;

import com.camp.camping_service.dto.common.ImageObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class CreateCampingRequest {
    private String title;
    private Long price;
    private String description;
    private String category;
    private BigDecimal lat;
    private BigDecimal lng;
    private ImageObject image;
}
