package com.camp.camping_service.controllers;

import com.camp.camping_service.dto.request.ProfileRequest;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.services.UserService;
import com.camp.camping_service.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<Object> createProfile(
            @RequestBody ProfileRequest req,
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        userService.upsertProfile(req,userPrincipal);
        return ResponseHelper.success("created profile");
    }
}
