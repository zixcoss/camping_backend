package com.camp.camping_service.services.implement;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.request.CheckoutRequest;
import com.camp.camping_service.entities.Booking;
import com.camp.camping_service.entities.Landmark;
import com.camp.camping_service.exceptions.CommonException;
import com.camp.camping_service.repositories.BookingRepository;
import com.camp.camping_service.services.PaymentService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${client.domain}")
    private String clientDomain;
    private final StripeClient stripeClient;
    private final BookingRepository bookingRepo;

    @Override
    public String checkOut(CheckoutRequest request) {
        Booking booking = bookingRepo.findById(request.getCode())
                .orElseThrow(() -> new CommonException(
                        ResponseMessage.FAIL_BOOKING_001.getMessage(),
                        ResponseMessage.FAIL_BOOKING_001.name(),
                        HttpStatus.NOT_FOUND
                ));
        Landmark landmark = booking.getLandmark();

        Builder productDataBuilder = new Builder()
                .setName(landmark.getTitle())
                .setDescription(landmark.getDescription());

        if(landmark.getSecureUrl() != null){
            productDataBuilder.addImage(landmark.getSecureUrl());
        }

        PriceData priceDataParam = PriceData.builder()
                .setCurrency("thb")
                .setProductData(productDataBuilder.build())
                .setUnitAmount(booking.getTotal() * 100)
                .build();

        Map<String ,String> metadata = new HashMap<>();
        metadata.put("bookingCode", booking.getId());

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .putAllMetadata(metadata)
                        .setReturnUrl(clientDomain + "/user/complete/{CHECKOUT_SESSION_ID}")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(priceDataParam)
                                        .setQuantity(1L)
                                        .build())
                        .build();

        try{
            Session session = stripeClient.v1().checkout().sessions().create(params);
            return session.getClientSecret();
        } catch (StripeException e) {
            throw new RuntimeException("stripe exception: "+e.getMessage());
        }

    }

    @Override
    public String checkoutStatus(String sessionId) {
        try{
            Session session = stripeClient.v1().checkout().sessions().retrieve(sessionId);
            Map<String , String> metadata = session.getMetadata();
            String bookingCode = metadata.get("bookingCode");
            if(!session.getStatus().equals("complete") || bookingCode == null){
                throw new CommonException(
                        ResponseMessage.FAIL_BOOKING_002.getMessage(),
                        ResponseMessage.FAIL_BOOKING_002.name(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

            Booking booking = bookingRepo.findById(bookingCode)
                    .orElseThrow(() -> new CommonException(
                            ResponseMessage.FAIL_BOOKING_001.getMessage(),
                            ResponseMessage.FAIL_BOOKING_001.name(),
                            HttpStatus.NOT_FOUND
                            )
                    );

            booking.setPaymentStatus(true);
            bookingRepo.save(booking);
            return session.getStatus();
        }catch (StripeException e){
            throw new RuntimeException("stripe exception: "+e.getMessage());
        }
    }
}


