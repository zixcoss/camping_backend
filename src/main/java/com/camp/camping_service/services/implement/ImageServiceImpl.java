package com.camp.camping_service.services.implement;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.ImageObject;
import com.camp.camping_service.exceptions.CommonException;
import com.camp.camping_service.services.ImageService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public ImageObject upload(MultipartFile file) {
        try{
            Map<String,Object> option = new HashMap<>();
            option.put("folder","Landmark");
            option.put("display_name",file.getOriginalFilename());
            Map imageResult = cloudinary.uploader().upload(file.getBytes(),option);
            return ImageObject.builder()
                    .publicId(imageResult.get("public_id").toString())
                    .secureUrl(imageResult.get("secure_url").toString())
                    .build();
        } catch (Exception e) {
            log.info("error in image service: {}",e.getMessage());
            throw new CommonException(
                    ResponseMessage.FAIL_IMAGE_001.getMessage(),
                    ResponseMessage.FAIL_IMAGE_001.name(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
