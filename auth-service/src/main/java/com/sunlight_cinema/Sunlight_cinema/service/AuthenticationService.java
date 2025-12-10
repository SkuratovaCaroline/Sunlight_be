package com.sunlight_cinema.Sunlight_cinema.service;

import com.sunlight_cinema.Sunlight_cinema.client.UserServiceClient;
import com.sunlight_cinema.Sunlight_cinema.dto.RegisterRequest;
import com.sunlight_cinema.Sunlight_cinema.dto.UserResponse;
import com.sunlight_cinema.Sunlight_cinema.model.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserServiceClient userServiceClient;
    private final ConfirmationTokenService tokenService;
    private final EmailService emailService;

    public void register(RegisterRequest request, Locale locale) {
        // 1. Создаём пользователя в user-service
        UserResponse userResponse = userServiceClient.createUser(request);

        // 2. Генерируем токен подтверждения — передаём ID пользователя (Long)
        ConfirmationToken token = tokenService.generateConfirmationToken(userResponse.id());

        // 3. Отправляем письмо
        emailService.sendConfirmationEmail(userResponse.email(), token.getToken());
    }
}