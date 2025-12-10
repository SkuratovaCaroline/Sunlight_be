package com.sunlight_cinema.Sunlight_cinema.client;

import com.sunlight_cinema.Sunlight_cinema.dto.RegisterRequest;
import com.sunlight_cinema.Sunlight_cinema.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {

    @PostMapping("/api/users")
    UserResponse createUser(@RequestBody RegisterRequest request);
}