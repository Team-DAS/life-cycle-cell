package com.freelance.lifecycle.notificationservice.dto;

import com.freelance.lifecycle.notificationservice.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private Long userId;
    private String message;
    private Boolean isRead;
    private NotificationType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
