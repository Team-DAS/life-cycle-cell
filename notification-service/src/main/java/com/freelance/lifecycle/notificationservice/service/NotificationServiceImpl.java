package com.freelance.lifecycle.notificationservice.service;

import com.freelance.lifecycle.notificationservice.dto.NotificationDTO;
import com.freelance.lifecycle.notificationservice.dto.NotificationEventDTO;
import com.freelance.lifecycle.notificationservice.exception.NotificationNotFoundException;
import com.freelance.lifecycle.notificationservice.model.Notification;
import com.freelance.lifecycle.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationDTO createNotification(NotificationEventDTO eventDTO) {
        log.info("Creating notification for user {} with type {}", eventDTO.getUserId(), eventDTO.getType());
        
        Notification notification = new Notification();
        notification.setUserId(eventDTO.getUserId());
        notification.setMessage(eventDTO.getMessage());
        notification.setType(eventDTO.getType());
        notification.setIsRead(false);
        
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created with ID: {}", savedNotification.getId());
        
        return mapToDTO(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsForUser(Long userId) {
        log.info("Retrieving notifications for user {}", userId);
        
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.info("Found {} notifications for user {}", notifications.size(), userId);
        
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO markAsRead(Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));
        
        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        
        log.info("Notification {} marked as read", notificationId);
        return mapToDTO(updatedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadCountForUser(Long userId) {
        log.debug("Getting unread count for user {}", userId);
        
        Long count = notificationRepository.countByUserIdAndIsRead(userId, false);
        log.debug("User {} has {} unread notifications", userId, count);
        
        return count;
    }

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.getIsRead());
        dto.setType(notification.getType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        return dto;
    }
}
