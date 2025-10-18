package com.freelance.lifecycle.notificationservice.messaging;

import com.freelance.lifecycle.notificationservice.dto.NotificationEventDTO;
import com.freelance.lifecycle.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

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
            throw new RuntimeException("Failed to process notification event", e);
        }
    }

    @Component("rabbitListenerErrorHandler")
    public static class RabbitListenerErrorHandlerImpl implements RabbitListenerErrorHandler {

        @Override
        public Object handleError(Message amqpMessage,
                                  org.springframework.messaging.Message<?> message,
                                  Exception ex) {
            log.error("RabbitMQ listener error: {}", ex.getMessage(), ex);

            // Aquí podrías enviar el mensaje a una Dead Letter Queue (DLQ)
            // o implementar lógica de reintento.
            // return null -> reconoce el mensaje y evita reprocesarlo.
            return null;
        }
    }
}
