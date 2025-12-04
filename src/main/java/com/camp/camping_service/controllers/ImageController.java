package com.camp.camping_service.controllers;

import com.camp.camping_service.services.ImageService;
import com.camp.camping_service.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/images/upload")
    public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile file){
        return ResponseHelper.successWithData("upload success",imageService.upload(file));
    }
}
