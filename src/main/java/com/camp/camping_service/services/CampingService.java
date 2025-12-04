package com.camp.camping_service.services;

import com.camp.camping_service.dto.request.CreateCampingRequest;
import com.camp.camping_service.dto.request.FavoriteRequest;
import com.camp.camping_service.dto.response.CampingListResponse;
import com.camp.camping_service.dto.response.CampingResponse;
import com.camp.camping_service.entities.Landmark;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface CampingService {
    void createCamping(CreateCampingRequest request, String clerkId);
    Map<String,Object> getListCamping(String clerkId);
    CampingResponse getCamping(String id);
    String addOrRemoveFavorite(FavoriteRequest request, String clerkId);
    List<CampingListResponse> getListMyFavorite(String clerkId);
    Map<String,Object> getListCampingWithFilter(String category, String search, String clerkId);
}
