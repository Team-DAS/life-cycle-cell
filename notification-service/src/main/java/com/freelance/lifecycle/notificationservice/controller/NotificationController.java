package com.freelance.lifecycle.notificationservice.controller;

import com.freelance.lifecycle.notificationservice.dto.NotificationDTO;
import com.freelance.lifecycle.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsForUser(@PathVariable Long userId) {
        log.info("GET request for notifications of user {}", userId);
        
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(userId);
        
        log.info("Returning {} notifications for user {}", notifications.size(), userId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        log.info("PATCH request to mark notification {} as read", id);
        
        NotificationDTO notification = notificationService.markAsRead(id);
        
        log.info("Notification {} marked as read", id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        log.debug("GET request for unread count of user {}", userId);
        
        Long count = notificationService.getUnreadCountForUser(userId);
        
        log.debug("User {} has {} unread notifications", userId, count);
        return ResponseEntity.ok(count);
    }
}
