package com.cinema.notification.service;

import com.cinema.notification.repository.NotificationRepository;
import com.cinema.notification.model.Notification;
import com.cinema.notification.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private WebSocketHandler webSocketHandler;

    public void sendNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        repository.save(notification);
        webSocketHandler.broadcast(message);
    }
}