package com.freelance.lifecycle.notificationservice.messaging;

import com.freelance.lifecycle.notificationservice.dto.NotificationEventDTO;
import com.freelance.lifecycle.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

@Component
@RequiredArgsConstructor
@Slf4j
@Validated
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notifications.queue", errorHandler = "rabbitListenerErrorHandler")
    public void handleNotificationEvent(@Valid NotificationEventDTO eventDTO) {
        try {
            log.info("Received notification event for user {} with type {}", 
                    eventDTO.getUserId(), eventDTO.getType());
            
            notificationService.createNotification(eventDTO);
            
            log.info("Successfully processed notification event for user {}", eventDTO.getUserId());
            
        } catch (Exception e) {
            log.error("Error processing notification event for user {}: {}", 
                    eventDTO.getUserId(), e.getMessage(), e);
            throw new ListenerExecutionFailedException("Failed to process notification event", e);
        }
    }

    @Component("rabbitListenerErrorHandler")
    public static class RabbitListenerErrorHandler implements org.springframework.amqp.rabbit.listener.RabbitListenerErrorHandler {
        
        @Override
        public Object handleError(org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException ex) {
            log.error("RabbitMQ listener error: {}", ex.getMessage(), ex);
            
            // Return null to acknowledge the message and prevent reprocessing
            // In production, you might want to implement dead letter queue logic here
            return null;
        }
    }
}
