package com.camp.camping_service.services;

import com.camp.camping_service.dto.common.ImageObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageService {
    ImageObject upload(MultipartFile file);
}
