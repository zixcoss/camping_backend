package com.camp.camping_service.utils;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.UserPrincipal;
import com.camp.camping_service.exceptions.ClerkException;
import com.camp.camping_service.exceptions.CommonException;
import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.helpers.security.AuthenticateRequest;
import com.clerk.backend_api.helpers.security.models.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.security.models.RequestState;
import com.clerk.backend_api.models.components.EmailAddress;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.operations.GetUserListRequest;
import com.clerk.backend_api.models.operations.GetUserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClerkUtil {

    @Value("${clerk.api-key}")
    private String secretKey;
    private final Clerk clerk;

    public boolean verifyToken(RequestState request){
        return request.reason().isEmpty();
    }

    public String getUsernameFromToken(RequestState request){
        if(request.claims().isPresent()){
            return request.claims().get().getSubject();
        }else {
            return null;
        }
    }

    public User getUserByClerkId(String clerkId){
        try{
            GetUserResponse res = clerk.users().get()
                    .userId(clerkId)
                    .call();
            if(res.user().isEmpty()){
                return null;
            }
            return res.user().get();
        }catch (Exception ex){
            log.info("user not found from clerk");
            throw new CommonException(
                    ResponseMessage.FAIL_AUTH_001.getMessage(),
                    ResponseMessage.FAIL_AUTH_001.name(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    public String getPrimaryEmail(User clerkUser){
        Optional<EmailAddress> primaryEmail = clerkUser.emailAddresses().stream()
                .filter(email -> email.id().equals(clerkUser.primaryEmailAddressId()))
                .findFirst();
        if(primaryEmail.isEmpty()){
            log.info("primary email is not found");
            throw new CommonException(
                    ResponseMessage.FAIL_AUTH_001.getMessage(),
                    ResponseMessage.FAIL_AUTH_001.name(),
                    HttpStatus.UNAUTHORIZED
            );
        }
        return primaryEmail.get().emailAddress();
    }

    public RequestState getRequestState(HttpServletRequest request) {
        Map<String, List<String>> headerMap = convertHeaders(request);
        AuthenticateRequestOptions options = AuthenticateRequestOptions
                .secretKey(secretKey)
                .build();
        return AuthenticateRequest.authenticateRequest(headerMap, options);
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
