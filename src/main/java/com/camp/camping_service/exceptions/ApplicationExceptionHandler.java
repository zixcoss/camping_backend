package com.camp.camping_service.exceptions;

import com.camp.camping_service.constants.ResponseMessage;
import com.camp.camping_service.dto.common.ErrorObject;
import com.camp.camping_service.dto.common.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ResponseObject res = ResponseObject.builder()
                .status(false)
                .message(ResponseMessage.FAILURE.getMessage())
                .errors(new ArrayList<>(
                        Collections.singletonList(
                                ErrorObject.builder()
                                        .code(ResponseMessage.FAIL_AUTH_001.name())
                                        .message(ResponseMessage.FAIL_AUTH_001.getMessage())
                                        .build()
                        )
                ))
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(res));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Object> handleCommonException(CommonException ex){
        log.warn("Common Exception: {}", ex.getMessage());
        ResponseObject res = ResponseObject.builder()
                .status(false)
                .message(ResponseMessage.FAILURE.getMessage())
                .errors(new ArrayList<>(
                        Collections.singletonList(
                                ErrorObject.builder()
                                        .code(ex.getCode())
                                        .message(ex.getMessage())
                                        .build()
                        )
                ))
                .build();

        return ResponseEntity.status(ex.getStatus() != null ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception ex){
        log.warn("Handling Exception: {}", ex.getMessage());
        ResponseObject resErr = ResponseObject.builder()
                .status(false)
                .message(ResponseMessage.FAILURE.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resErr);
    }
}
