package com.camp.camping_service.controllers;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.request.CheckoutRequest;
import com.camp.camping_service.services.PaymentService;
import com.camp.camping_service.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<Object> checkOut(@RequestBody CheckoutRequest request){
        return ResponseHelper.successWithData(
                ResponseMessage.SUCCESS.getMessage(),
                paymentService.checkOut(request)
        );
    }

    @GetMapping("/checkout-status/{session_id}")
    public ResponseEntity<Object> checkOutStatus(@PathVariable(name = "session_id") String sessionId) {
        return ResponseHelper.successWithData(
                "Payment success",
                paymentService.checkoutStatus(sessionId)
        );
    }
}
