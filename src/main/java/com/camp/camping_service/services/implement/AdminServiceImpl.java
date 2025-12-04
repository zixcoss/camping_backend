package com.camp.camping_service.services.implement;

import com.camp.camping_service.dto.response.*;
import com.camp.camping_service.entities.Booking;
import com.camp.camping_service.entities.Landmark;
import com.camp.camping_service.repositories.BookingRepository;
import com.camp.camping_service.repositories.LandmarkRepository;
import com.camp.camping_service.repositories.ProfileRepository;
import com.camp.camping_service.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ProfileRepository profileRepo;
    private final LandmarkRepository landmarkRepo;
    private final BookingRepository bookingRepo;

    public StatsCountResponse getStats(){
        final long usersCount = profileRepo.count();
        final long landmarkCount = landmarkRepo.count();
        final long bookingCount = bookingRepo.count();
        return StatsCountResponse.builder()
                .usersCount(usersCount)
                .campingCount(landmarkCount)
                .bookingCount(bookingCount)
                .build();
    }

    @Override
    public ReservationResponse getReservationsStats(String clerkId) {
        final long landmarkCount = landmarkRepo.countByProfileId(clerkId);
        final List<Booking> bookings = bookingRepo.findByLandmarkProfileId(clerkId);
        long nights = 0;
        long totals = 0;
        for(Booking booking: bookings){
            nights += booking.getTotalNights();
            totals += booking.getTotal();
        }
        return ReservationResponse.builder()
                .campingCount(landmarkCount)
                .nights(nights)
                .totals(totals)
                .build();
    }

    @Override
    public List<ReservationListResponse> getReservationsList(String clerkId) {
        final List<Booking> bookings = bookingRepo.findByPaymentStatusTrueAndLandmarkProfileIdOrderByCreateAtDesc(clerkId);
        return bookings.stream().map((booking)-> ReservationListResponse.builder()
                .bookingCode(booking.getId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .paymentStatus(booking.getPaymentStatus())
                .total(booking.getTotal())
                .totalNight(booking.getTotalNights())
                .landmarkCode(booking.getLandmarkId())
                .landmarkTitle(booking.getLandmark().getTitle())
                .build()
        ).toList();
    }

    @Override
    public List<MyCampingResponse> getMyCamping(String clerkId) {
        final List<Landmark> landmarks = landmarkRepo.findByProfileId(clerkId);
        return landmarks.stream().map(l -> MyCampingResponse.builder()
                .code(l.getId())
                .title(l.getTitle())
                .price(l.getPrice())
                .createAt(l.getCreateAt())
                .updateAt(l.getUpdateAt())
                .build()
        ).toList();
    }
}
