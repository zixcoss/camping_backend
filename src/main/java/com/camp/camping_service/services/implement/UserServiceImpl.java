package com.camp.camping_service.services.implement;

import com.camp.camping_service.dto.request.ProfileRequest;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.entities.Profile;
import com.camp.camping_service.repositories.ProfileRepository;
import com.camp.camping_service.services.UserService;
import com.camp.camping_service.utils.ClerkUtil;
import com.clerk.backend_api.models.components.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ClerkUtil clerk;
    private final ProfileRepository profileRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFromClerk = clerk.getUserByClerkId(username);
        String email = clerk.getPrimaryEmail(userFromClerk);
        return UserPrincipal.builder()
                .email(email)
                .clerkId(userFromClerk.id())
                .profileImage(userFromClerk.imageUrl().orElse(null))
                .build();
    }

    @Override
    public void upsertProfile(ProfileRequest request, UserPrincipal userPrincipal) {
        Optional<Profile> profileOpt = profileRepo.findByClerkId(userPrincipal.getClerkId());
        Profile profile;
        if(profileOpt.isPresent()){
            profile = profileOpt.get();
            profile.setEmail(userPrincipal.getEmail());
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            profile.setProfileImage(userPrincipal.getProfileImage());
            log.info("update profile...");
        }else{
            profile = Profile.builder()
                    .clerkId(userPrincipal.getClerkId())
                    .email(userPrincipal.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .profileImage(userPrincipal.getProfileImage())
                    .build();
            log.info("insert profile...");
        }
        profileRepo.save(profile);
        log.info("upsert method success");
    }
}
