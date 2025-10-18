# Notification Service

Microservicio Spring Boot para gestionar notificaciones y alertas a usuarios basado en la arquitectura orientada a células.

## Descripción

Este microservicio maneja las notificaciones de usuarios del sistema, incluyendo alertas sobre nuevas postulaciones, cambios de estado, mensajes y finalización de proyectos. Utiliza RabbitMQ para comunicación asíncrona con otros servicios.

## Tecnologías Utilizadas

- **Spring Boot 3.2.0**
- **Java 17**
- **Gradle**
- **Spring Data JPA**
- **PostgreSQL**
- **RabbitMQ**
- **Bean Validation**
- **Lombok**

## Estructura del Proyecto

```
src/main/java/com/freelance/lifecycle/notificationservice/
├── NotificationServiceApplication.java
├── config/
│   └── RabbitMQConfig.java
├── controller/
│   └── NotificationController.java
├── dto/
│   ├── NotificationDTO.java
│   └── NotificationEventDTO.java
├── exception/
│   ├── NotificationNotFoundException.java
│   └── GlobalExceptionHandler.java
├── messaging/
│   └── NotificationEventConsumer.java
├── model/
│   ├── Notification.java
│   └── NotificationType.java
├── repository/
│   └── NotificationRepository.java
└── service/
    ├── NotificationService.java
    └── NotificationServiceImpl.java
```

## Configuración

### Base de Datos

El servicio está configurado para conectarse a PostgreSQL en la base de datos `sql7`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sql7
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
```

### RabbitMQ

Configuración para comunicación asíncrona:

```yaml
spring:
  rabbitmq:
    host: ${RABBITMQ_HOST:localhost}
    port: ${RABBITMQ_PORT:5672}
    username: ${RABBITMQ_USERNAME:guest}
    password: ${RABBITMQ_PASSWORD:guest}
```

### Variables de Entorno

- `DB_USERNAME`: Usuario de la base de datos (default: postgres)
- `DB_PASSWORD`: Contraseña de la base de datos (default: password)
- `RABBITMQ_HOST`: Host de RabbitMQ (default: localhost)
- `RABBITMQ_PORT`: Puerto de RabbitMQ (default: 5672)
- `RABBITMQ_USERNAME`: Usuario de RabbitMQ (default: guest)
- `RABBITMQ_PASSWORD`: Contraseña de RabbitMQ (default: guest)

## API Endpoints

### GET /api/v1/notifications/user/{userId}
Obtiene todas las notificaciones de un usuario.

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "userId": 123,
    "message": "Nueva postulación recibida",
    "isRead": false,
    "type": "NEW_APPLICATION",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": null
  }
]
```

### PATCH /api/v1/notifications/{id}/read
Marca una notificación como leída.

**Response:** 200 OK
```json
{
  "id": 1,
  "userId": 123,
  "message": "Nueva postulación recibida",
  "isRead": true,
  "type": "NEW_APPLICATION",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T11:00:00"
}
```

### GET /api/v1/notifications/user/{userId}/unread-count
Obtiene el número de notificaciones no leídas de un usuario.

**Response:** 200 OK
```json
5
```

## Tipos de Notificaciones

El enum `NotificationType` define los siguientes tipos:

- **NEW_APPLICATION**: Nueva postulación recibida
- **APPLICATION_ACCEPTED**: Postulación aceptada
- **NEW_MESSAGE**: Nuevo mensaje recibido
- **PROJECT_COMPLETED**: Proyecto completado

## Comunicación Asíncrona

### RabbitMQ Queue

El servicio escucha en la cola `notifications.queue` para recibir eventos de otros servicios.

### Evento de Notificación

```json
{
  "userId": 123,
  "message": "Tu postulación ha sido aceptada",
  "type": "APPLICATION_ACCEPTED"
}
```

### Publicación de Eventos

Otros servicios pueden publicar eventos usando:

```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void publishNotificationEvent(NotificationEventDTO event) {
    rabbitTemplate.convertAndSend("notifications.queue", event);
}
```

## Manejo de Errores

El servicio incluye manejo global de excepciones:

- **404 Not Found**: Cuando no se encuentra una notificación
- **400 Bad Request**: Para validaciones fallidas
- **500 Internal Server Error**: Para errores inesperados

## Ejecución

1. Asegúrate de tener PostgreSQL ejecutándose con la base de datos `sql7`
2. Asegúrate de tener RabbitMQ ejecutándose
3. Configura las variables de entorno si es necesario
4. Ejecuta el proyecto:

```bash
./gradlew bootRun
```

El servicio estará disponible en `http://localhost:8083`

## Testing

Para ejecutar las pruebas:

```bash
./gradlew test
```

## Docker

Para ejecutar con Docker:

```bash
docker build -t notification-service .
docker run -p 8083:8083 notification-service
```
