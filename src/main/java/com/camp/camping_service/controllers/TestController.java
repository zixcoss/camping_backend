package com.camp.camping_service.controllers;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.exceptions.CommonException;
import com.camp.camping_service.utils.ResponseHelper;
import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.helpers.security.AuthenticateRequest;
import com.clerk.backend_api.helpers.security.models.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.security.models.RequestState;
import com.clerk.backend_api.models.components.EmailAddress;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.errors.ClerkErrors;
import com.clerk.backend_api.models.operations.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.Object;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
@CrossOrigin
public class TestController {

    private final Clerk clerk;

    @Value("${clerk.api-key}")
    private String secretKey;

    @GetMapping("/clerk-sessions")
    public ResponseEntity<Object> testClerkSessionList() throws ClerkErrors,Exception {
        GetSessionListRequest req = GetSessionListRequest.builder()
                .userId("user_34ekvaVZo5JyAbRZsMEXfSou49z")
                .build();
        GetSessionListResponse res = clerk.sessions().list().request(req).call();
        if(res.sessionList().isPresent()){
            return ResponseHelper.successWithData("Clerk session fetched successfully", res.sessionList().get());
        }
        return ResponseHelper.failure("Failed to fetch Clerk sessions", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-users")
    public ResponseEntity<Object> testGetUsers() throws Exception {
        List<String> email = new ArrayList<>();
        email.add("watcharit64@gmail.com");
        log.info("email address : {}", email);
        GetUserListRequest request = GetUserListRequest.builder()
                .emailAddress(email)
                .build();
        GetUserListResponse response = clerk.users().list().request(request).call();
        if(response.userList().isEmpty()){
            throw new CommonException(
                    "TEST_USER_01",
                    "user not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return ResponseHelper.successWithList(ResponseMessage.SUCCESS.getMessage(), new ArrayList<>(response.userList().get()));
    }

    @GetMapping("/get-user")
    public ResponseEntity<Object> testGetUser() throws Exception {
        GetUserResponse res = clerk.users().get().userId("user_34ekvaVZo5JyAbRZsMEXfSou49z").call();
        if(res.user().isEmpty()){
            throw new CommonException(
                    "TEST_USER_01",
                    "user not found",
                    HttpStatus.NOT_FOUND
            );
        }
        User userFromClerk = res.user().get();
        Optional<EmailAddress> primaryEmail = userFromClerk.emailAddresses().stream()
                .filter(email -> email.id().equals(userFromClerk.primaryEmailAddressId()))
                .findFirst();
        return primaryEmail.map(emailAddress -> ResponseHelper.successWithData(ResponseMessage.SUCCESS.getMessage(), emailAddress.emailAddress())).orElse(null);
    }

    @GetMapping("/clerk-verify")
    public ResponseEntity<Object> testClerkVerify(){
        try{
            if(RequestContextHolder.getRequestAttributes() != null){
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                Map<String , List<String>> headerMap = convertHeaders(request);
                String token = headerMap.get(HttpHeaders.AUTHORIZATION.toLowerCase()).get(0);
                log.info("token is {}",token);
                RequestState requestState = AuthenticateRequest.authenticateRequest(headerMap, AuthenticateRequestOptions
                        .secretKey(secretKey)
                        .build());

                return ResponseHelper.successWithData("success",requestState.claims());
            }else{
                return ResponseHelper.failure("request is null",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.failure("Clerk client verified failed",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, List<String>> convertHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);

            List<String> list = new ArrayList<>();
            while (values.hasMoreElements()) {
                list.add(values.nextElement());
            }

            headers.put(name, list);
        }

        return headers;
    }
}
