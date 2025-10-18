# API Gateway

Microservicio Spring Cloud Gateway que actúa como punto de entrada único para el sistema Life Cycle Cell.

## Descripción

El API Gateway es el punto de entrada único para todos los microservicios del sistema. Utiliza Spring Cloud Gateway para enrutar las peticiones a los servicios correspondientes, proporcionando una interfaz unificada y centralizada.

## Tecnologías Utilizadas

- **Spring Boot 3.2.0**
- **Spring Cloud Gateway**
- **Java 17**
- **Gradle**
- **Spring Boot Actuator**

## Estructura del Proyecto

```
src/main/java/com/freelance/lifecycle/apigateway/
├── ApiGatewayApplication.java
└── src/main/resources/
    └── application.yml
```

## Configuración

### Puerto
El gateway está configurado para ejecutarse en el puerto **8000**.

### Rutas Configuradas

#### Application Service
- **Path:** `/api/v1/applications/**`
- **Destino:** `http://application-service:8080`
- **Filtro:** `StripPrefix=2` (elimina `/api/v1`)

#### Notification Service
- **Path:** `/api/v1/notifications/**`
- **Destino:** `http://notification-service:8083`
- **Filtro:** `StripPrefix=2` (elimina `/api/v1`)

#### Project Service (Futuro)
- **Path:** `/api/v1/projects/**`
- **Destino:** `http://project-service:8081`
- **Filtro:** `StripPrefix=2` (elimina `/api/v1`)

#### User Service (Futuro)
- **Path:** `/api/v1/users/**`
- **Destino:** `http://user-service:8082`
- **Filtro:** `StripPrefix=2` (elimina `/api/v1`)

## Funcionalidades

### Enrutamiento
- Enruta automáticamente las peticiones a los microservicios correspondientes
- Utiliza predicados de path para determinar el destino
- Aplica filtros para modificar las peticiones antes del enrutamiento

### CORS
- Configuración global de CORS habilitada
- Permite todas las orígenes, métodos y headers
- Soporte para credenciales

### Health Checks
- Endpoint de salud disponible en `/actuator/health`
- Información detallada de salud en modo desarrollo
- Endpoint de información del gateway en `/actuator/gateway`

## Ejemplos de Uso

### Acceso a Application Service
```bash
# A través del Gateway
curl http://localhost:8000/api/v1/applications

# Se enruta internamente a:
# http://application-service:8080/applications
```

### Acceso a Notification Service
```bash
# A través del Gateway
curl http://localhost:8000/api/v1/notifications/user/123

# Se enruta internamente a:
# http://notification-service:8083/notifications/user/123
```

## Endpoints de Gestión

### Health Check
```bash
curl http://localhost:8000/actuator/health
```

### Información del Gateway
```bash
curl http://localhost:8000/actuator/gateway/routes
```

### Información de la Aplicación
```bash
curl http://localhost:8000/actuator/info
```

## Configuración Avanzada

### Logging
El gateway está configurado con logging detallado para:
- `org.springframework.cloud.gateway`: DEBUG
- `org.springframework.web.reactive`: DEBUG
- `reactor.netty`: DEBUG

### Filtros Disponibles
- **StripPrefix:** Elimina segmentos del path antes del enrutamiento
- **AddRequestHeader:** Agrega headers a las peticiones
- **AddResponseHeader:** Agrega headers a las respuestas
- **Retry:** Reintenta peticiones fallidas

## Ejecución

### Desarrollo Local
```bash
./gradlew bootRun
```

### Docker
```bash
docker build -t api-gateway .
docker run -p 8000:8000 api-gateway
```

### Docker Compose
```bash
docker-compose up -d api-gateway
```

## Arquitectura

```
Client Request
     ↓
API Gateway (Port 8000)
     ↓
┌─────────────────┬─────────────────┬─────────────────┐
│ Application     │ Notification    │ Project         │
│ Service         │ Service         │ Service         │
│ (Port 8080)     │ (Port 8083)     │ (Port 8081)     │
└─────────────────┴─────────────────┴─────────────────┘
```

## Beneficios

1. **Punto de Entrada Único:** Todos los clientes acceden a través del gateway
2. **Enrutamiento Centralizado:** Configuración centralizada de rutas
3. **CORS Unificado:** Manejo centralizado de CORS
4. **Monitoreo:** Visibilidad centralizada del tráfico
5. **Seguridad:** Punto único para implementar autenticación y autorización
6. **Load Balancing:** Distribución de carga entre instancias de servicios

## Próximas Mejoras

- **Autenticación JWT:** Implementar autenticación centralizada
- **Rate Limiting:** Limitar peticiones por cliente
- **Circuit Breaker:** Implementar patrón circuit breaker
- **Service Discovery:** Integración con Eureka o Consul
- **Logging Centralizado:** Integración con sistemas de logging
- **Métricas:** Integración con Prometheus y Grafana
