package com.camp.camping_service.services;

import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.dto.request.BookingRequest;
import com.camp.camping_service.dto.response.BookingListResponse;

import java.util.List;

public interface BookingService {
    String createBooking(UserPrincipal userPrincipal, BookingRequest request);
    List<BookingListResponse> listBooking(UserPrincipal userPrincipal);
}
