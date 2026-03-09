package com.camp.camping_service.services.implement;

import com.camp.camping_service.dto.common.ImageObject;
import com.camp.camping_service.dto.request.CreateCampingRequest;
import com.camp.camping_service.dto.request.UpdateCampingRequest;
import com.camp.camping_service.dto.response.CampingListResponse;
import com.camp.camping_service.dto.response.CampingResponse;
import com.camp.camping_service.dto.select.SelectLandmarkListRecord;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
                            .secureUrl("http://secureurl/pre-upload/image")
                            .publicId("public-123")
                            .build()
            );

            final Profile testProfile = Profile.builder()
                    .clerkId(clerkId)
                    .firstName("John")
                    .lastName("Doe")
                    .email("test@mail.com")
                    .build();

            //given
            when(profileRepo.findByClerkId(anyString())).thenReturn(Optional.of(testProfile));

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

            //given
            when(profileRepo.findByClerkId(anyString())).thenReturn(Optional.of(testProfile));

            //when
            campingService.createCamping(request, clerkId);

            //then
            verify(profileRepo, times(1)).findByClerkId(anyString());
            verify(landmarkRepo, times(1)).save(any());
            verifyNoInteractions(imageService);
        }

        @Test
        @DisplayName("should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound(){
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
            //given
            when(profileRepo.findByClerkId(anyString())).thenReturn(Optional.empty());
            //when & then
            final CommonException exception = assertThrows(CommonException.class, () -> {
                campingService.createCamping(request,clerkId);
            });

            assertNotNull(exception);
            assertEquals("FAIL_USER_001", exception.getCode());
            assertEquals("user not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

            verify(profileRepo, times(1)).findByClerkId(anyString());
            verifyNoInteractions(landmarkRepo);
            verifyNoInteractions(imageService);
        }
    }

    @Nested
    @DisplayName("update camping test")
    class UpdateCampingTesting{

        @Test
        @DisplayName("should update camping success when image not change")
        void shouldUpdateCampingSuccessWhenImageNotChange(){
            //setup
            final UpdateCampingRequest request = new UpdateCampingRequest();
            request.setCode("landmark-123");
            request.setTitle("test update");
            request.setDescription("testing update landmark");
            request.setPrice(1200L);
            request.setCategory("hotel");
            request.setLat(BigDecimal.valueOf(100));
            request.setLng(BigDecimal.valueOf(130));
            request.setImage(
                    ImageObject.builder()
                            .secureUrl("http://secureurl/landmark/image")
                            .publicId("public-123")
                            .build()
            );

            final Landmark testLandmark = Landmark.builder()
                    .id("landmark-123")
                    .title("test")
                    .description("test")
                    .price(1000L)
                    .category("hotel")
                    .lat(BigDecimal.valueOf(100))
                    .lng(BigDecimal.valueOf(130))
                    .publicId("public-123")
                    .secureUrl("http://secureurl/landmark/image")
                    .profileId("clerk-123")
                    .build();
            //given
            when(landmarkRepo.findById(anyString())).thenReturn(Optional.of(testLandmark));

            //when
            campingService.updateCamping(request);

            //then
            assertEquals(testLandmark.getTitle(), request.getTitle());
            assertEquals(testLandmark.getDescription(), request.getDescription());
            assertEquals(testLandmark.getPrice(), request.getPrice());

            verify(landmarkRepo, times(1)).findById(anyString());
            verify(landmarkRepo,times(1)).save(any());
            verifyNoInteractions(imageService);
        }

        @Test
        @DisplayName("should update camping success when image change")
        void shouldUpdateCamingSuccessWhenImageChange(){
            //setup
            final UpdateCampingRequest request = new UpdateCampingRequest();
            request.setCode("landmark-123");
            request.setTitle("test update");
            request.setDescription("testing update landmark");
            request.setPrice(1200L);
            request.setCategory("hotel");
            request.setLat(BigDecimal.valueOf(100));
            request.setLng(BigDecimal.valueOf(130));
            request.setImage(
                    ImageObject.builder()
                            .secureUrl("http://secureurl/pre-upload/image2")
                            .publicId("public-456")
                            .build()
            );

            final Landmark testLandmark = Landmark.builder()
                    .id("landmark-123")
                    .title("test")
                    .description("test")
                    .price(1000L)
                    .category("hotel")
                    .lat(BigDecimal.valueOf(100))
                    .lng(BigDecimal.valueOf(130))
                    .publicId("public-123")
                    .secureUrl("http://secureurl/landmark/image")
                    .profileId("clerk-123")
                    .build();

            final ImageObject image = ImageObject.builder()
                    .secureUrl("http://secureurl/landmark/image2")
                    .publicId("public-456")
                    .build();

            //given
            when(landmarkRepo.findById(anyString())).thenReturn(Optional.of(testLandmark));
            when(imageService.moveFile(anyString(),anyString(),anyString())).thenReturn(image);

            //when
            campingService.updateCamping(request);

            //then
            assertEquals(testLandmark.getTitle(), request.getTitle());
            assertEquals(testLandmark.getDescription(), request.getDescription());
            assertEquals(testLandmark.getPrice(), request.getPrice());

            verify(landmarkRepo, times(1)).findById(anyString());
            verify(landmarkRepo,times(1)).save(any());
            verify(imageService, times(1)).deleteFile(anyString());
            verify(imageService, times(1)).moveFile(anyString(),anyString(),anyString());
        }

        @Test
        @DisplayName("should throw exception when landmark not found")
        void shouldThrowExceptionWhenLandmarkNotFound(){
            //setup
            final UpdateCampingRequest request = new UpdateCampingRequest();
            request.setCode("landmark-123");
            request.setTitle("test update");
            request.setDescription("testing update landmark");
            request.setPrice(1200L);
            request.setCategory("hotel");
            request.setLat(BigDecimal.valueOf(100));
            request.setLng(BigDecimal.valueOf(130));
            request.setImage(
                    ImageObject.builder()
                            .secureUrl("http://secureurl/pre-upload/image2")
                            .publicId("public-456")
                            .build()
            );
            //given
            when(landmarkRepo.findById(anyString())).thenReturn(Optional.empty());

            //when & then
            final CommonException exception = assertThrows(CommonException.class, () -> {
                campingService.updateCamping(request);
            });

            assertNotNull(exception);
            assertEquals("FAIL_CAMPING_001", exception.getCode());
            assertEquals("camping not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

            verify(landmarkRepo, times(1)).findById(anyString());
            verify(landmarkRepo, never()).save(any());
            verifyNoInteractions(imageService);
        }
    }

    @Nested
    @DisplayName("get list camping test")
    class GetListCampingTesting{
        @Test
        @DisplayName("should get list camping successfully")
        void shouldGetListCampingSuccessfully(){
            //setup
            final String clarkId = "clark-123";
            final List<SelectLandmarkListRecord> testLandmarkList = List.of(
                    new SelectLandmarkListRecord("landmark-123","test1","test1",1000L, BigDecimal.valueOf(100L),BigDecimal.valueOf(10L),"http://secure/image1","fav-123"),
                    new SelectLandmarkListRecord("landmark-456","test2","test2",1000L, BigDecimal.valueOf(120L),BigDecimal.valueOf(20L),"http://secure/image2","fav-234")
            );

            //given
            when(landmarkRepo.findAllWithFavorite(anyString(),isNull(),eq(""))).thenReturn(testLandmarkList);

            //when
            Map<String, Object> result = campingService.getListCamping(clarkId);

            //then
            assertNotNull(result);
            assertNotNull(result.get("landmarks"));
            assertNotNull(result.get("center"));

            Object landmarksObj = result.get("landmarks");
            List<?> landmarks = assertInstanceOf(List.class, landmarksObj);
            assertEquals(testLandmarkList.size(), landmarks.size());

            verify(landmarkRepo, times(1)).findAllWithFavorite(anyString(), isNull(), eq(""));
        }

        @Test
        @DisplayName("should get list camping successfully when no information")
        void shouldGetListCampingSuccessfullyWhenNoInformation(){
            //setup
            final String clarkId = "clark-123";

            //given
            when(landmarkRepo.findAllWithFavorite(anyString(),isNull(),eq(""))).thenReturn(List.of());

            //when
            Map<String, Object> result = campingService.getListCamping(clarkId);

            //then
            assertNotNull(result);
            assertNotNull(result.get("landmarks"));
            assertNull(result.get("center"));

            Object landmarksObj = result.get("landmarks");
            List<?> landmarks = assertInstanceOf(List.class, landmarksObj);
            assertEquals(0, landmarks.size());

            verify(landmarkRepo, times(1)).findAllWithFavorite(anyString(), isNull(), eq(""));
        }
    }

    @Nested
    @DisplayName("get camping test")
    class GetCampingTest{
        @Test
        @DisplayName("get camping successfully")
        void GetCampingSuccessfully(){
            //setup
            final String landmarkId = "landmark-123";
            final Landmark testLandmark = Landmark.builder()
                    .id("landmark-123")
                    .title("test")
                    .description("test")
                    .price(1000L)
                    .category("hotel")
                    .lat(BigDecimal.valueOf(100))
                    .lng(BigDecimal.valueOf(130))
                    .publicId("public-123")
                    .secureUrl("http://secureurl/landmark/image")
                    .profileId("clerk-123")
                    .build();

            //given
            when(landmarkRepo.findById(anyString())).thenReturn(Optional.of(testLandmark));

            //when
            CampingResponse result = campingService.getCamping(landmarkId);

            //then
            assertNotNull(result);
            assertEquals(testLandmark.getId(),result.getCode());
            assertEquals(testLandmark.getTitle(), result.getTitle());

            verify(landmarkRepo, times(1)).findById(anyString());
        }

        @Test
        @DisplayName("get camping when camping not found")
        void GetCampingWhenCampingNotFound(){
            //setup
            final String landmarkId = "landmark-123";

            //given
            when(landmarkRepo.findById(anyString())).thenReturn(Optional.empty());

            //when & then
            final CommonException exception = assertThrows(CommonException.class, () -> {
                campingService.getCamping(landmarkId);
            });

            assertNotNull(exception);
            assertEquals("FAIL_CAMPING_001", exception.getCode());
            assertEquals("camping not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

            verify(landmarkRepo, times(1)).findById(anyString());
        }
    }
}