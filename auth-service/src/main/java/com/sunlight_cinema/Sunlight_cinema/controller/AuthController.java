package com.sunlight_cinema.Sunlight_cinema.controller;

import com.sunlight_cinema.Sunlight_cinema.dto.JwtResponse;
import com.sunlight_cinema.Sunlight_cinema.dto.LoginRequest;
import com.sunlight_cinema.Sunlight_cinema.dto.RegisterRequest;
import com.sunlight_cinema.Sunlight_cinema.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // ...
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        try {
            JwtResponse jwtResponse = authenticationService.login(loginRequest, response);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            Locale locale = request.getLocale();
            String msg = messageSource.getMessage("auth.login.error", new Object[]{e.getMessage()}, locale);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, null, msg));
        }
    }

    // ← Здесь НЕТ лишней закрывающей скобки!

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletResponse response) {
        try {
            JwtResponse jwtResponse = authenticationService.refreshToken(request, response);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            Locale locale = request.getLocale();
            String msg = messageSource.getMessage("auth.refresh.error", null, locale);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, null, msg));
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Auth service is alive!";
    }
}