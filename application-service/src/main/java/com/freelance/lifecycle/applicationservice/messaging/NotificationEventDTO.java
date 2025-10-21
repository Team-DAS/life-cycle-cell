package com.freelance.lifecycle.applicationservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventDTO {
    private Long userId;
    private String message;
    private String type; // Use String to avoid cross-module enum dependency
}
