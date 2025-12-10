package com.sunlight_cinema.user.controller;

import com.sunlight_cinema.user.dto.UserDto;
import com.sunlight_cinema.user.dto.UserProfileDto;
import com.sunlight_cinema.user.logging.LogExecutionTime;
import com.sunlight_cinema.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @GetMapping
    @LogExecutionTime
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(userService.findByName(name));
        }
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findProfileById(id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @LogExecutionTime
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(userDto));
    }

    @PutMapping("/edit")
    @PreAuthorize("authentication.name == #userDto.login() or hasAuthority('ADMIN')")
    @LogExecutionTime
    public ResponseEntity<UserDto> updateUser(
            @ModelAttribute UserDto userDto,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return ResponseEntity.ok(userService.update(userDto, avatar));
    }
}