package com.camp.camping_service.services.implement;

import com.camp.camping_service.dto.common.ImageObject;
import com.camp.camping_service.dto.request.CreateCampingRequest;
import com.camp.camping_service.entities.Landmark;
import com.camp.camping_service.entities.Profile;
import com.camp.camping_service.exceptions.CommonException;
import com.camp.camping_service.repositories.FavoriteRepository;
import com.camp.camping_service.repositories.LandmarkRepository;
import com.camp.camping_service.repositories.ProfileRepository;
import com.camp.camping_service.services.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("camping service unit test")
class CampingServiceTest {
    @Mock
    private LandmarkRepository landmarkRepo;
    @Mock
    private ProfileRepository profileRepo;
    @Mock
    private FavoriteRepository favoriteRepo;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private CampingServiceImpl campingService;

    @Nested
    @DisplayName("create camping test")
    class CreateCampingTesting{

        @Test
        @DisplayName("should create camping successfully")
        void shouldCreateCampingSuccessfully(){
            //setup
            final String clerkId = "clerk-123";
            final CreateCampingRequest request = new CreateCampingRequest();
            request.setTitle("test");
            request.setDescription("testing create landmark");
            request.setPrice(1000L);
            request.setCategory("hotel");
            request.setLat(BigDecimal.valueOf(100));
            request.setLng(BigDecimal.valueOf(130));
            request.setImage(
                    ImageObject.builder()
                            .secureUrl("http://secureurl/image")
                            .publicId("public-123")
                            .build()
            );

            final Profile testProfile = Profile.builder()
                    .clerkId(clerkId)
                    .firstName("John")
                    .lastName("Doe")
                    .email("test@mail.com")
                    .build();

            final Landmark testLandmark = Landmark.builder()
                    .id("landmark-123")
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .category(request.getCategory())
                    .lat(request.getLat())
                    .lng(request.getLng())
                    .publicId(request.getImage().getPublicId())
                    .secureUrl(request.getImage().getSecureUrl())
                    .profileId(testProfile.getClerkId())
                    .build();

            //given
            when(profileRepo.findByClerkId(anyString())).thenReturn(Optional.of(testProfile));
            when(landmarkRepo.save(any())).thenReturn(testLandmark);

            //when
            campingService.createCamping(request, clerkId);

            //then
            verify(profileRepo, times(1)).findByClerkId(anyString());
            verify(landmarkRepo, times(1)).save(any());
            verify(imageService, times(1)).moveFile(anyString(),anyString(), anyString());
        }


        @Test
        @DisplayName("should create camping successfully and no image")
        void shouldCreateCampingSuccessfullyAndNoImage(){
            //setup
            final String clerkId = "clerk-123";
            final CreateCampingRequest request = new CreateCampingRequest();
            request.setTitle("test");
            request.setDescription("testing create landmark");
            request.setPrice(1000L);
            request.setCategory("hotel");
            request.setLat(BigDecimal.valueOf(100));
            request.setLng(BigDecimal.valueOf(130));
            request.setImage(null);

            final Profile testProfile = Profile.builder()
                    .clerkId(clerkId)
                    .firstName("John")
                    .lastName("Doe")
                    .email("test@mail.com")
                    .build();

            final Landmark testLandmark = Landmark.builder()
                    .id("landmark-123")
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .category(request.getCategory())
                    .lat(request.getLat())
                    .lng(request.getLng())
                    .publicId(null)
                    .secureUrl(null)
                    .profileId(testProfile.getClerkId())
                    .build();

            //given
            when(profileRepo.findByClerkId(anyString())).thenReturn(Optional.of(testProfile));
            when(landmarkRepo.save(any())).thenReturn(testLandmark);

            //when
            campingService.createCamping(request, clerkId);

            //then
            verify(profileRepo, times(1)).findByClerkId(anyString());
            verify(landmarkRepo, times(1)).save(any());
            verifyNoInteractions(imageService);
        }

        @Test
        @DisplayName("should create camping when user not found")
        void shouldThrowExceptionWhenUserNotFound(){
            final String clerkId = "clerk-123";

            final CreateCampingRequest request = new CreateCampingRequest();
            request.setTitle("test");
            request.setDescription("testing create landmark");
            request.setPrice(1000L);
            request.setCategory("hotel");
            request.setLat(BigDecimal.valueOf(100));
            request.setLng(BigDecimal.valueOf(130));
            request.setImage(null);

            when(profileRepo.findByClerkId(anyString())).thenReturn(Optional.empty());

            final CommonException exception = assertThrows(CommonException.class, () -> {
                campingService.createCamping(request,clerkId);
            });

            assertEquals("FAIL_USER_001", exception.getCode());
            assertEquals("user not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

            verify(profileRepo, times(1)).findByClerkId(anyString());
            verifyNoInteractions(landmarkRepo);
            verifyNoInteractions(imageService);
        }
    }

}