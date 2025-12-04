package com.camp.camping_service.services.implement;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.dto.request.BookingRequest;
import com.camp.camping_service.dto.response.BookingListResponse;
import com.camp.camping_service.entities.Booking;
import com.camp.camping_service.entities.Landmark;
import com.camp.camping_service.entities.Profile;
import com.camp.camping_service.exceptions.CommonException;
import com.camp.camping_service.repositories.BookingRepository;
import com.camp.camping_service.repositories.LandmarkRepository;
import com.camp.camping_service.repositories.ProfileRepository;
import com.camp.camping_service.services.BookingService;
import com.camp.camping_service.utils.CalBooking.TotalResult;
import com.camp.camping_service.utils.CalBooking;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final LandmarkRepository landmarkRepo;
    private final ProfileRepository profileRepo;

    @Override
    @Transactional
    public String createBooking(UserPrincipal userPrincipal, BookingRequest request) {
        bookingRepo.deleteAllByProfileIdAndPaymentStatus(userPrincipal.getClerkId(), false);
        Landmark landmark = landmarkRepo.findById(request.getCampingCode())
                .orElseThrow(()->new CommonException(
                        ResponseMessage.FAIL_CAMPING_001.getMessage(),
                        ResponseMessage.FAIL_CAMPING_001.name(),
                        HttpStatus.NOT_FOUND
                ));
        Profile profile = profileRepo.findByClerkId(userPrincipal.getClerkId())
                .orElseThrow(()->new CommonException(
                        ResponseMessage.FAIL_USER_001.getMessage(),
                        ResponseMessage.FAIL_USER_001.name(),
                        HttpStatus.NOT_FOUND
                ));

        TotalResult result = CalBooking.calTotal(request.getCheckIn(),request.getCheckOut(),landmark.getPrice());
        Booking newBooking = Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .total(result.getTotal())
                .totalNights(result.getTotalNight())
                .profileId(profile.getClerkId())
                .landmarkId(landmark.getId())
                .build();
        Booking booking = bookingRepo.save(newBooking);
        return booking.getId();
    }

    @Override
    public List<BookingListResponse> listBooking(UserPrincipal userPrincipal) {
        List<Booking> bookings = bookingRepo.findByProfileIdAndPaymentStatusTrueOrderByCheckInDesc(userPrincipal.getClerkId());
        return bookings.stream().map((booking)-> BookingListResponse.builder()
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

}
