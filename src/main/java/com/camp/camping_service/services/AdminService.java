package com.camp.camping_service.services;

import com.camp.camping_service.dto.response.MyCampingResponse;
import com.camp.camping_service.dto.response.ReservationListResponse;
import com.camp.camping_service.dto.response.ReservationResponse;
import com.camp.camping_service.dto.response.StatsCountResponse;

import java.util.List;

public interface AdminService {
    StatsCountResponse getStats();
    ReservationResponse getReservationsStats(String clerkId);
    List<ReservationListResponse> getReservationsList(String clerkId);
    List<MyCampingResponse> getMyCamping(String clerkId);
}
