package com.freelance.lifecycle.notificationservice.service;

import com.freelance.lifecycle.notificationservice.dto.NotificationDTO;
import com.freelance.lifecycle.notificationservice.dto.NotificationEventDTO;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(NotificationEventDTO eventDTO);

    List<NotificationDTO> getNotificationsForUser(Long userId);

    NotificationDTO markAsRead(Long notificationId);

    Long getUnreadCountForUser(Long userId);
}
