package com.skuratova.cinema.controller;

import com.skuratova.cinema.entity.*;
import com.skuratova.cinema.repository.*;
import com.skuratova.cinema.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Controllers {

    @Autowired private UserRepository userRepo;
    @Autowired private FilmRepository filmRepo;
    @Autowired private FileStorageService fileService;

    // ---------- USERS ----------
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepo.save(user);
    }

    @PostMapping("/users/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile file) throws IOException {
        User user = userRepo.findById(id).orElseThrow();
        String path = fileService.saveAvatar(file);
        user.setUAvatar(path);
        userRepo.save(user);
        return ResponseEntity.ok("Аватарка сохранена");
    }

    @PutMapping("/users/{id}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile file) throws IOException {
        return uploadAvatar(id, file);
    }

    @GetMapping("/users/{id}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) throws IOException {
        User user = userRepo.findById(id).orElseThrow();
        byte[] data = fileService.loadFile(user.getUAvatar());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(data);
    }

    // ---------- FILMS ----------
    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmRepo.findAll();
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        return filmRepo.save(film);
    }

    @PostMapping("/films/{id}/poster")
    public ResponseEntity<?> uploadPoster(@PathVariable Long id, @RequestParam("poster") MultipartFile file) throws IOException {
        Film film = filmRepo.findById(id).orElseThrow();
        String path = fileService.savePoster(file);
        film.setFPoster(path);
        filmRepo.save(film);
        return ResponseEntity.ok("Постер сохранён");
    }

    @GetMapping("/films/{id}/poster")
    public ResponseEntity<byte[]> getPoster(@PathVariable Long id) throws IOException {
        Film film = filmRepo.findById(id).orElseThrow();
        byte[] data = fileService.loadFile(film.getFPoster());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(data);
    }
}
