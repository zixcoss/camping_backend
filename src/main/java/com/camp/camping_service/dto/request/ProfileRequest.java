package com.camp.camping_service.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileRequest {
    private String firstName;
    private String lastName;
}
