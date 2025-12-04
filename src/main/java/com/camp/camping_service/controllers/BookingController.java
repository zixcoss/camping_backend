package com.camp.camping_service.controllers;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.dto.request.BookingRequest;
import com.camp.camping_service.services.BookingService;
import com.camp.camping_service.utils.ResponseHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<Object> createBooking(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody BookingRequest request
    ){
        return ResponseHelper.successWithData(
                "create booking success",
                bookingService.createBooking(userPrincipal,request)
        );
    }

    @GetMapping("/booking")
    public ResponseEntity<Object> listBooking(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseHelper.successWithList(
                ResponseMessage.SUCCESS.getMessage(),
                bookingService.listBooking(userPrincipal)
        );
    }
}
