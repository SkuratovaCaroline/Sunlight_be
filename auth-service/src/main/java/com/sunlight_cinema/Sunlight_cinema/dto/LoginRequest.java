// auth-service/src/main/java/com/sunlight_cinema/Sunlight_cinema/dto/LoginRequest.java
package com.sunlight_cinema.Sunlight_cinema.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}