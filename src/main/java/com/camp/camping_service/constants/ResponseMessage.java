package com.camp.camping_service.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SUCCESS("Operation completed successfully."),
    FAILURE("Operation failed. Please try again."),

    FAIL_AUTH_001("token expired or invalid"),

    FAIL_USER_001("user not found"),

    FAIL_CAMPING_001("camping not found"),

    FAIL_IMAGE_001("upload image unsuccess"),

    FAIL_BOOKING_001("booking not found"),
    FAIL_BOOKING_002("booking payment failed"),
    ;
    private final String message;
}
