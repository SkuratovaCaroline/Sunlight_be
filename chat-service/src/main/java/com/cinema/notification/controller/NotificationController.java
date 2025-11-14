package com.cinema.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.cinema.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody String message) {
        service.sendNotification(message);
        return ResponseEntity.ok().build();
    }
}
