package com.freelance.lifecycle.notificationservice.dto;

import com.freelance.lifecycle.notificationservice.model.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Message is required")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType type;
}
