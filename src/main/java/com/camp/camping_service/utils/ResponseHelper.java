package com.camp.camping_service.utils;

import com.camp.camping_service.dto.common.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHelper {
    ResponseHelper(){throw new IllegalArgumentException(); }

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    public static ResponseEntity<Object> response(HttpStatus status, Object body) {
        HttpHeaders headers = new HttpHeaders();
        if(RequestContextHolder.getRequestAttributes() != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            headers.add("Request-Date", request.getHeader("Request-Date"));
        }
        headers.add("Response-Date", LocalDateTime.now().format(dateTimeFormat));
        return ResponseEntity.status(status).headers(headers).body(body);
    }

    public static ResponseEntity<Object> success(String msg){
        ResponseObject res = ResponseObject.builder()
                .status(true)
                .message(msg)
                .build();
        return response(HttpStatus.OK, res);
    }

    public static ResponseEntity<Object> successWithData(String msg, Object obj){
        Map<String, Object> data = new HashMap<>();
        data.put("status", true);
        data.put("message", msg);
        data.put("data", obj);
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> successWithList(String msg, Object list){
        Map<String , Object> data = new HashMap<>();
        data.put("status", true);
        data.put("message", msg);
        data.put("data", list);
        return response(HttpStatus.OK, data);
    }

    public static ResponseEntity<Object> failure(String msg, HttpStatus status){
        ResponseObject res = ResponseObject.builder()
                .status(false)
                .message(msg)
                .build();
        return response(status, res);
    }
}
