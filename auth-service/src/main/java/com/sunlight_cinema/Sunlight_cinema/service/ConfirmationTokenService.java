package com.sunlight_cinema.Sunlight_cinema.service;

import com.sunlight_cinema.Sunlight_cinema.model.ConfirmationToken;
import com.sunlight_cinema.Sunlight_cinema.repository.ConfirmationTokenRepository;
import com.sunlight_cinema.user.model.User;
import com.sunlight_cinema.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    // Принимаем Long (ID пользователя), а не объект User
    public ConfirmationToken generateConfirmationToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        String tokenValue = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(tokenValue);
        confirmationToken.setUser((org.apache.catalina.User) user);           // теперь правильно — объект User
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        return confirmationTokenRepository.save(confirmationToken);
    }

    private ConfirmationToken validateConfirmationToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid confirmation token"));

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Expired confirmation token");
        }

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already confirmed");
        }

        return confirmationToken;
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = validateConfirmationToken(token);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        User user = (User) confirmationToken.getUser();
        user.setEmailVerified(true);  // у тебя уже есть это поле!
        userRepository.save(user);
    }
}