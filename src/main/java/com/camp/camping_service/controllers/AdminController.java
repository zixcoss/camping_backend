package com.camp.camping_service.controllers;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.services.AdminService;
import com.camp.camping_service.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<Object> statsStats(){
        return ResponseHelper.successWithData(
                ResponseMessage.SUCCESS.getMessage(),
                adminService.getStats()
        );
    }

    @GetMapping("/reservations")
    public ResponseEntity<Object> reservationsStats(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseHelper.successWithData(
                ResponseMessage.SUCCESS.getMessage(),
                adminService.getReservationsStats(userPrincipal.getClerkId())
        );
    }

    @GetMapping("/reservations-list")
    public ResponseEntity<Object> reservationsList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseHelper.successWithList(
                ResponseMessage.SUCCESS.getMessage(),
                adminService.getReservationsList(userPrincipal.getClerkId())
        );
    }

    @GetMapping("/my-camping")
    public ResponseEntity<Object> myCamping(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseHelper.successWithList(
                ResponseMessage.SUCCESS.getMessage(),
                adminService.getMyCamping(userPrincipal.getClerkId())
        );
    }
}
