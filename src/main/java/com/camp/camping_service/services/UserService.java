package com.camp.camping_service.services;

import com.camp.camping_service.dto.request.ProfileRequest;
import com.camp.camping_service.dto.common.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void upsertProfile(ProfileRequest request, UserPrincipal userPrincipal);
}
