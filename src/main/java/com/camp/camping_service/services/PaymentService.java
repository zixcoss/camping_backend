package com.camp.camping_service.services;

import com.camp.camping_service.dto.request.CheckoutRequest;

public interface PaymentService {
    String checkOut(CheckoutRequest request);
    String checkoutStatus(String sessionId);
}
