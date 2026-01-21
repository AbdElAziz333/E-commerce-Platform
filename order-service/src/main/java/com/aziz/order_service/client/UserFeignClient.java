package com.aziz.order_service.client;

import com.aziz.order_service.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface UserFeignClient {
    @GetMapping("/api/v1/users/current/email")
    ApiResponse<String> getCurrentUserEmail(@RequestHeader("User-Id") Long userId);
}