package com.camp.camping_service.controllers;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.request.CreateCampingRequest;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.dto.request.FavoriteRequest;
import com.camp.camping_service.services.CampingService;
import com.camp.camping_service.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CampingController {

    private final CampingService campingService;

    @PostMapping("/create-camping")
    public ResponseEntity<Object> createCamping(
            @RequestBody CreateCampingRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        campingService.createCamping(request, userPrincipal.getClerkId());
        return ResponseHelper.success("Landmark created");
    }

    @GetMapping("/campings/{clerkId}")
    public ResponseEntity<Object> getListCamping(@PathVariable(name = "clerkId") String clerkId){
        return ResponseHelper.successWithList(ResponseMessage.SUCCESS.getMessage(),
                campingService.getListCamping(clerkId)
        );
    }

    @GetMapping("/camping/{code}")
    public ResponseEntity<Object> getCamping(@PathVariable(name = "code") String code){
        return ResponseHelper.successWithData(ResponseMessage.SUCCESS.getMessage(),
                campingService.getCamping(code)
        );
    }

    @PostMapping("/favorite")
    public ResponseEntity<Object> addOrRemoveFavorite(
            @RequestBody FavoriteRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        return ResponseHelper.success(campingService.addOrRemoveFavorite(request,userPrincipal.getClerkId()));
    }

    @GetMapping("/favorites")
    public ResponseEntity<Object> getListMyFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseHelper.successWithList(
                    ResponseMessage.SUCCESS.getMessage(),
                    campingService.getListMyFavorite(userPrincipal.getClerkId())
                );
    }

    @GetMapping("/filter-camping/{clerkId}")
    public ResponseEntity<Object> getListCampingWithFilter(
            @RequestParam(name = "category",required = false) String category,
            @RequestParam(name = "search",required = false) String search,
            @PathVariable(name = "clerkId") String clerkId
    ){
        return ResponseHelper.successWithList(
                ResponseMessage.SUCCESS.getMessage(),
                campingService.getListCampingWithFilter(category,search,clerkId)
        );
    }
}
