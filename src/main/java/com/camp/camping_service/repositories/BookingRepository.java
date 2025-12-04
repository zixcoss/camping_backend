package com.camp.camping_service.repositories;

import com.camp.camping_service.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    void deleteAllByProfileIdAndPaymentStatus(String id, Boolean paymentStatus);
    List<Booking> findByPaymentStatusTrueAndLandmarkProfileIdOrderByCreateAtDesc(String clerkId);
    List<Booking> findByLandmarkProfileId(String clerkId);
    List<Booking> findByProfileIdAndPaymentStatusTrueOrderByCheckInDesc(String clerkId);
}
