package com.camp.camping_service.dto.common;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageObject {
    private String secureUrl;
    private String publicId;
}
