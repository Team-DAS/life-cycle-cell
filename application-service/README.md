# Application Service

Microservicio Spring Boot para gestionar las postulaciones a proyectos basado en la Célula 43.

## Descripción

Este microservicio maneja las postulaciones de freelancers a proyectos, permitiendo crear, consultar y actualizar el estado de las aplicaciones.

## Tecnologías Utilizadas

- **Spring Boot 3.2.0**
- **Java 17**
- **Gradle**
- **Spring Data JPA**
- **PostgreSQL**
- **Bean Validation**
- **Lombok**

## Estructura del Proyecto

```
src/main/java/com/freelance/lifecycle/applicationservice/
├── ApplicationServiceApplication.java
├── controller/
│   └── ApplicationController.java
├── dto/
│   ├── ApplicationRequestDTO.java
│   ├── ApplicationResponseDTO.java
│   └── StatusUpdateDTO.java
├── exception/
│   ├── ApplicationNotFoundException.java
│   ├── InvalidStatusException.java
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Application.java
│   └── ApplicationStatus.java
├── repository/
│   └── ApplicationRepository.java
└── service/
    ├── ApplicationService.java
    └── ApplicationServiceImpl.java
```

## Configuración

### Base de Datos

El servicio está configurado para conectarse a PostgreSQL en la base de datos `sql4`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sql4
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
```

### Variables de Entorno

- `DB_USERNAME`: Usuario de la base de datos (default: postgres)
- `DB_PASSWORD`: Contraseña de la base de datos (default: password)

## API Endpoints

### POST /api/v1/applications
Crea una nueva postulación.

**Request Body:**
```json
{
  "projectId": 1,
  "freelancerId": 2,
  "message": "Estoy interesado en este proyecto..."
}
```

**Response:** 201 Created
```json
{
  "id": 1,
  "projectId": 1,
  "freelancerId": 2,
  "employerId": 1,
  "message": "Estoy interesado en este proyecto...",
  "status": "PENDING",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": null
}
```

### PATCH /api/v1/applications/{id}/status
Actualiza el estado de una postulación.

**Request Body:**
```json
{
  "status": "ACCEPTED"
}
```

**Response:** 200 OK
```json
{
  "id": 1,
  "projectId": 1,
  "freelancerId": 2,
  "employerId": 1,
  "message": "Estoy interesado en este proyecto...",
  "status": "ACCEPTED",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T11:00:00"
}
```

### GET /api/v1/applications/project/{projectId}
Obtiene todas las postulaciones de un proyecto específico.

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "projectId": 1,
    "freelancerId": 2,
    "employerId": 1,
    "message": "Estoy interesado en este proyecto...",
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": null
  }
]
```

### GET /api/v1/applications/user/{freelancerId}
Obtiene todas las postulaciones de un freelancer específico.

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "projectId": 1,
    "freelancerId": 2,
    "employerId": 1,
    "message": "Estoy interesado en este proyecto...",
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": null
  }
]
```

## Estados de Postulación

El enum `ApplicationStatus` define los siguientes estados:

- **PENDING**: Postulación creada, pendiente de revisión
- **VIEWED**: Postulación vista por el empleador
- **ACCEPTED**: Postulación aceptada
- **REJECTED**: Postulación rechazada

### Transiciones de Estado Válidas

- PENDING → VIEWED, ACCEPTED, REJECTED
- VIEWED → ACCEPTED, REJECTED
- ACCEPTED → No se puede cambiar
- REJECTED → No se puede cambiar

## Manejo de Errores

El servicio incluye manejo global de excepciones:

- **404 Not Found**: Cuando no se encuentra una postulación
- **400 Bad Request**: Para validaciones fallidas o transiciones de estado inválidas
- **500 Internal Server Error**: Para errores inesperados

## Ejecución

1. Asegúrate de tener PostgreSQL ejecutándose con la base de datos `sql4`
2. Configura las variables de entorno si es necesario
3. Ejecuta el proyecto:

```bash
./gradlew bootRun
```

El servicio estará disponible en `http://localhost:8080`

## Testing

Para ejecutar las pruebas:

```bash
./gradlew test
```