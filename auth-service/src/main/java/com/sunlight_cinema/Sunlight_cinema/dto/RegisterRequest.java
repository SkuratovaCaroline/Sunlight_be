package com.sunlight_cinema.Sunlight_cinema.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 6) String password,
        @Email String email,
        String firstName,
        String lastName
) {}