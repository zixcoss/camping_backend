package com.camp.camping_service.services.implement;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.ImageObject;
import com.camp.camping_service.dto.request.CreateCampingRequest;
import com.camp.camping_service.dto.request.FavoriteRequest;
import com.camp.camping_service.dto.request.UpdateCampingRequest;
import com.camp.camping_service.dto.response.CampingListResponse;
import com.camp.camping_service.dto.response.CampingListResponse.Center;
import com.camp.camping_service.dto.response.CampingResponse;
import com.camp.camping_service.dto.select.SelectLandmarkListRecord;
import com.camp.camping_service.entities.Favorite;
import com.camp.camping_service.entities.Landmark;
import com.camp.camping_service.entities.Profile;
import com.camp.camping_service.exceptions.CommonException;
import com.camp.camping_service.repositories.FavoriteRepository;
import com.camp.camping_service.repositories.LandmarkRepository;
import com.camp.camping_service.repositories.ProfileRepository;
import com.camp.camping_service.services.CampingService;
import com.camp.camping_service.services.ImageService;
import com.camp.camping_service.utils.GeoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampingServiceImpl implements CampingService {

    private final LandmarkRepository landmarkRepo;
    private final ProfileRepository profileRepo;
    private final FavoriteRepository favoriteRepo;
    private final ImageService imageService;

    @Override
    public void createCamping(CreateCampingRequest request, String clerkId) {
        Profile profile = profileRepo.findByClerkId(clerkId).orElseThrow(()->
                new CommonException(
                    ResponseMessage.FAIL_USER_001.getMessage(),
                    ResponseMessage.FAIL_USER_001.name(),
                    HttpStatus.NOT_FOUND
                ));

        ImageObject image = null;

        if(request.getImage() != null){
           image = imageService.moveFile(request.getImage().getPublicId(), "Pre-upload", "Landmark");
        }

        Landmark landmark = Landmark.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .lat(request.getLat())
                .lng(request.getLng())
                .publicId(image != null ? image.getPublicId() : null)
                .secureUrl(image != null ? image.getSecureUrl() : null)
                .profileId(profile.getClerkId())
                .build();

        landmarkRepo.save(landmark);
        log.info("create landmark success.");
    }

    @Override
    public void updateCamping(UpdateCampingRequest request) {

        Landmark landmark = landmarkRepo.findById(request.getCode()).orElseThrow(()->
                new CommonException(
                    ResponseMessage.FAIL_CAMPING_001.getMessage(),
                    ResponseMessage.FAIL_CAMPING_001.name(),
                    HttpStatus.NOT_FOUND
                ));

        landmark.setTitle(request.getTitle());
        landmark.setDescription(request.getDescription());
        landmark.setCategory(request.getCategory());
        landmark.setPrice(request.getPrice());
        landmark.setLat(request.getLat());
        landmark.setLng(request.getLng());

        if(request.getImage() != null){
            if(!request.getImage().getPublicId().equals(landmark.getPublicId())){
                imageService.deleteFile(landmark.getPublicId());
                ImageObject image = imageService.moveFile(request.getImage().getPublicId(),"Pre-upload", "Landmark");
                landmark.setPublicId(image.getPublicId());
                landmark.setSecureUrl(image.getSecureUrl());
            }
        }

        landmarkRepo.save(landmark);
        log.info("update landmark success.");
    }

    @Override
    public Map<String, Object> getListCamping(String clerkId) {
        List<SelectLandmarkListRecord> landmarks = landmarkRepo.findAllWithFavorite(clerkId,null,"");
        Point centerPoint = GeoUtil.getCenter(landmarks);
        List<CampingListResponse> landmarksResponse = landmarks.stream().map(
                l -> CampingListResponse.builder()
                        .code(l.code())
                        .title(l.title())
                        .description(l.description())
                        .imageUrl(l.imageUrl())
                        .price(l.price())
                        .lat(l.lat())
                        .lng(l.lng())
                        .isFavorite(l.isFavorite())
                        .build()
        ).toList();
        Center center = null;
        if(centerPoint != null){
            center = Center.builder()
                    .lat(centerPoint.getY())
                    .lng(centerPoint.getX())
                    .build();
        }
        Map<String,Object> data =new HashMap<>();
        data.put("landmarks",landmarksResponse);
        data.put("center", center);
        return data;
    }

    @Override
    public CampingResponse getCamping(String id) {
        Landmark landmark = landmarkRepo.findById(id).orElseThrow(()->
                        new CommonException(
                                ResponseMessage.FAIL_CAMPING_001.getMessage(),
                                ResponseMessage.FAIL_CAMPING_001.name(),
                                HttpStatus.NOT_FOUND
                        )
                );

        return CampingResponse.builder()
                .code(landmark.getId())
                .title(landmark.getTitle())
                .description(landmark.getDescription())
                .lat(landmark.getLat())
                .lng(landmark.getLng())
                .price(landmark.getPrice())
                .imageUrl(landmark.getSecureUrl())
                .image(landmark.getPublicId())
                .category(landmark.getCategory())
                .build();
    }

    @Override
    @Transactional
    public String addOrRemoveFavorite(FavoriteRequest request, String clerkId) {
        Profile profile = profileRepo.findByClerkId(clerkId).orElseThrow(()->
                new CommonException(
                        ResponseMessage.FAIL_USER_001.getMessage(),
                        ResponseMessage.FAIL_USER_001.name(),
                        HttpStatus.NOT_FOUND
                ));

        Landmark landmark = landmarkRepo.findById(request.getCampingCode()).orElseThrow(()->
                new CommonException(
                        ResponseMessage.FAIL_CAMPING_001.getMessage(),
                        ResponseMessage.FAIL_CAMPING_001.name(),
                        HttpStatus.NOT_FOUND
                )
        );

        if(request.getIsFavorite()){
            favoriteRepo.deleteAllByProfileIdAndLandmarkId(profile.getClerkId(),request.getCampingCode());
            return "Remove landmark in My favorite";
        }else{
            Favorite entity = Favorite.builder()
                    .profileId(profile.getClerkId())
                    .landmarkId(landmark.getId())
                    .build();
            favoriteRepo.save(entity);
            return "Add landmark in My favorite";
        }
    }

    @Override
    public List<CampingListResponse> getListMyFavorite(String clerkId) {
        List<Favorite> favorites = favoriteRepo.findByProfileId(clerkId);
        return favorites.stream().map(favorite -> {
            Landmark landmark = favorite.getLandmark();
            return CampingListResponse.builder()
                    .code(landmark.getId())
                    .title(landmark.getTitle())
                    .description(landmark.getDescription())
                    .price(landmark.getPrice())
                    .lat(landmark.getLat())
                    .lng(landmark.getLng())
                    .imageUrl(landmark.getSecureUrl())
                    .isFavorite(true)
                    .build();
            }
        ).toList();
    }

    @Override
    public Map<String,Object> getListCampingWithFilter(String category, String search,String clerkId) {
        List<SelectLandmarkListRecord> landmarks = landmarkRepo.findAllWithFavorite(
                clerkId,
                !category.equals("null") ? category : null,
                !search.equals("null") ? search : ""
        );
        Point centerPoint = GeoUtil.getCenter(landmarks);
        List<CampingListResponse> landmarksResponse = landmarks.stream().map(
                l -> CampingListResponse.builder()
                        .code(l.code())
                        .title(l.title())
                        .description(l.description())
                        .imageUrl(l.imageUrl())
                        .price(l.price())
                        .lat(l.lat())
                        .lng(l.lng())
                        .isFavorite(l.isFavorite())
                        .build()
        ).toList();
        Center center = null;
        if (centerPoint != null) {
            center = Center.builder()
                    .lat(centerPoint.getY())
                    .lng(centerPoint.getX())
                    .build();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("landmarks", landmarksResponse);
        data.put("center", center);
        return data;
    }
}
