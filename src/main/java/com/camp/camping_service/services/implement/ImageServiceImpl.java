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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
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
            option.put("folder","Pre-upload");
            option.put("display_name",file.getOriginalFilename());
            Map result = cloudinary.uploader().upload(file.getBytes(),option);
            return ImageObject.builder()
                    .publicId(result.get("public_id").toString())
                    .secureUrl(result.get("secure_url").toString())
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

    @Override
    public ImageObject moveFile(String publicId,String oldFolderName, String newFolderName) {
        String newPublicId = publicId.replace(oldFolderName+"/", newFolderName+"/");
        try{
            Map<String, Object> options = new HashMap<>();
            options.put("overwrite", true);
            options.put("to_type", "upload");
            options.put("invalidate", true);
            Map result = cloudinary.uploader().rename(publicId, newPublicId, options);
            cloudinary.api().update(result.get("public_id").toString(),Map.of("asset_folder", "Landmark"));
            log.info("move to landmark success.");
            return ImageObject.builder()
                    .publicId(result.get("public_id").toString())
                    .secureUrl(result.get("secure_url").toString())
                    .build();
        }catch (Exception e){
            throw new CommonException(
                    ResponseMessage.FAIL_IMAGE_002.getMessage(),
                    ResponseMessage.FAIL_IMAGE_002.name(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void cleanupPreUploadFiles() {
        long timeAgo = Instant.now().minus(2, ChronoUnit.MINUTES).getEpochSecond();
        try{
            Map result = cloudinary.search()
                    .expression("public_id:Pre-upload/* AND created_at < "+timeAgo)
                    .sortBy("created_at","asc")
                    .maxResults(100)
                    .execute();

            List<Map> resources =  (List<Map>) result.get("resources");

            if(resources.isEmpty()){
                log.info("No expired pre-upload files found.");
                return;
            }

            List<String> publicIdToDelete = resources.stream()
                    .map(res -> (String) res.get("public_id"))
                    .toList();

            cloudinary.api().deleteResources(publicIdToDelete, Map.of());
            log.info("Deleted {} expired files from Pre-upload folder.",publicIdToDelete.size());
        }catch (Exception e){
            log.error("Clean up folder pre-upload fail : {}",e.getMessage());
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try{
            cloudinary.uploader().destroy(publicId, Map.of());
            log.info("delete image success.");
        }catch (Exception e){
            log.error("delete image fail : {}", e.getMessage());
        }
    }
}
