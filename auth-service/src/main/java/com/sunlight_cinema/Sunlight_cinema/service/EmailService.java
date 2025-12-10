package com.sunlight_cinema.Sunlight_cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendConfirmationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Подтверждение регистрации – Sunlight Cinema");
            helper.setText("""
                <h2>Добро пожаловать!</h2>
                <p>Подтвердите email, перейдя по ссылке:</p>
                <a href="http://localhost:8081/api/auth/confirm?token=%s">Подтвердить</a>
                """.formatted(token), true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось отправить email", e);
        }
    }
}