# Life Cycle Cell - Arquitectura Orientada a Células

## 🏗️ Arquitectura del Sistema

Este proyecto implementa una arquitectura orientada a células (Cell-Oriented Architecture) para el sistema de gestión de freelancers, basado en la **Célula 43**.

### 🎯 Concepto de Células

Una **célula** es una unidad de negocio autónoma que contiene todos los componentes necesarios para cumplir una función específica:
- **Servicios de dominio**
- **Base de datos**
- **APIs REST**
- **Lógica de negocio**
- **Tests**

## 📁 Estructura del Proyecto

```
life-cycle-cell/
├── application-service/          # Célula de Postulaciones
│   ├── src/
│   ├── Dockerfile
│   ├── build.gradle
│   └── README.md
├── project-service/              # Célula de Proyectos (futuro)
├── user-service/                # Célula de Usuarios (futuro)
├── api-gateway/                 # Gateway API (futuro)
├── docker-compose.yml
├── build.gradle                 # Build raíz
├── settings.gradle
└── README.md
```

## 🧬 Células Implementadas

### 1. Application Service (Célula 43)
**Responsabilidad:** Gestión de postulaciones a proyectos

- **Puerto:** 8080
- **Base de datos:** PostgreSQL (sql4)
- **Endpoints:**
  - `POST /api/v1/applications` - Crear postulación
  - `PATCH /api/v1/applications/{id}/status` - Actualizar estado
  - `GET /api/v1/applications/project/{projectId}` - Postulaciones por proyecto
  - `GET /api/v1/applications/user/{freelancerId}` - Postulaciones por freelancer

### 2. Notification Service (Célula de Notificaciones)
**Responsabilidad:** Gestión de notificaciones y alertas

- **Puerto:** 8083
- **Base de datos:** PostgreSQL (sql7)
- **Message Broker:** RabbitMQ
- **Endpoints:**
  - `GET /api/v1/notifications/user/{userId}` - Notificaciones por usuario
  - `PATCH /api/v1/notifications/{id}/read` - Marcar como leída
  - `GET /api/v1/notifications/user/{userId}/unread-count` - Contador no leídas
- **Comunicación:** Asíncrona via RabbitMQ (cola: notifications.queue)

### 3. Project Service (Planificado)
**Responsabilidad:** Gestión de proyectos y empleadores

### 4. User Service (Planificado)
**Responsabilidad:** Gestión de usuarios (freelancers y empleadores)

### 5. API Gateway (Planificado)
**Responsabilidad:** Enrutamiento y autenticación centralizada

## 🚀 Inicio Rápido

### Prerrequisitos
- Docker y Docker Compose
- Java 17+ (para desarrollo local)
- PostgreSQL (si ejecutas servicios individualmente)

### Ejecutar con Docker Compose

1. **Clonar el repositorio:**
   ```bash
   git clone <repository-url>
   cd life-cycle-cell
   ```

2. **Configurar variables de entorno (opcional):**
   ```bash
   cp env.example .env
   # Editar .env según tus necesidades
   ```

3. **Ejecutar todos los servicios:**
   ```bash
   docker-compose up -d
   ```

4. **Ejecutar solo servicios activos:**
   ```bash
   docker-compose up -d postgres application-service
   ```

5. **Ver logs:**
   ```bash
   docker-compose logs -f application-service
   ```

### Desarrollo Local

1. **Ejecutar solo la base de datos:**
   ```bash
   docker-compose up -d postgres
   ```

2. **Ejecutar application-service localmente:**
   ```bash
   cd application-service
   ./gradlew bootRun
   ```

## 🔧 Configuración

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `DB_HOST` | Host de la base de datos | `postgres` |
| `DB_PORT` | Puerto de la base de datos | `5432` |
| `DB_NAME` | Nombre de la base de datos | `sql4` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de la base de datos | `password` |

### Puertos de Servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| PostgreSQL (sql4) | 5432 | Base de datos de aplicaciones |
| PostgreSQL (sql7) | 5433 | Base de datos de notificaciones |
| RabbitMQ | 5672 | Message broker |
| RabbitMQ Management | 15672 | Interfaz web de RabbitMQ |
| Application Service | 8080 | API de postulaciones |
| Notification Service | 8083 | API de notificaciones |
| Project Service | 8081 | API de proyectos (futuro) |
| User Service | 8082 | API de usuarios (futuro) |
| API Gateway | 8000 | Gateway principal (futuro) |

## 🧪 Testing

### Ejecutar Tests de un Servicio
```bash
cd application-service
./gradlew test
```

### Ejecutar Tests de Todos los Servicios
```bash
./gradlew test
```

## 📊 Monitoreo y Salud

### Health Checks
Cada servicio expone un endpoint de salud:
- Application Service: `http://localhost:8080/actuator/health`

### Logs Centralizados
```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f application-service
```

## 🔄 CI/CD

### Build y Deploy
```bash
# Build de todos los servicios
./gradlew build

# Build de un servicio específico
./gradlew :application-service:build

# Deploy con Docker
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

## 🏗️ Arquitectura de Datos

### Base de Datos por Célula
Cada célula mantiene su propia base de datos para garantizar:
- **Autonomía:** Cada célula puede evolucionar independientemente
- **Escalabilidad:** Servicios pueden escalarse por separado
- **Resiliencia:** Fallo de una célula no afecta otras

### Comunicación Entre Células
- **Síncrona:** REST APIs para operaciones críticas
- **Asíncrona:** Eventos para notificaciones y actualizaciones
- **Gateway:** Punto único de entrada para clientes externos

## 🎯 Próximos Pasos

1. **Implementar Project Service**
2. **Implementar User Service**
3. **Configurar API Gateway**
4. **Implementar autenticación JWT**
5. **Agregar monitoreo con Prometheus/Grafana**
6. **Configurar CI/CD con GitHub Actions**

## 📚 Documentación por Servicio

- [Application Service](./application-service/README.md)
- [Project Service](./project-service/README.md) (próximamente)
- [User Service](./user-service/README.md) (próximamente)

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-celula`)
3. Commit tus cambios (`git commit -am 'Agregar nueva célula'`)
4. Push a la rama (`git push origin feature/nueva-celula`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.
