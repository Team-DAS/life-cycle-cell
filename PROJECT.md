# Life Cycle Cell - Project Configuration

## 📋 Project Information
- **Name:** Life Cycle Cell
- **Description:** Arquitectura orientada a células para gestión de freelancers
- **Version:** 0.0.1-SNAPSHOT
- **Group:** com.freelance.lifecycle

## 🏗️ Architecture
- **Pattern:** Cell-Oriented Architecture
- **Base Cell:** Célula 43 (Application Management)
- **Services:** Microservices with Spring Boot
- **Database:** PostgreSQL per cell
- **Containerization:** Docker & Docker Compose

## 🚀 Services Status

| Service | Status | Port | Database | Description |
|---------|--------|------|----------|-------------|
| application-service | ✅ Active | 8080 | sql4 | Postulaciones management |
| project-service | 📋 Planned | 8081 | sql4 | Proyectos management |
| user-service | 📋 Planned | 8082 | sql4 | Usuarios management |
| api-gateway | 📋 Planned | 8000 | - | API Gateway |

## 🔧 Development Setup

### Prerequisites
- Java 17+
- Docker & Docker Compose
- PostgreSQL (for local development)

### Quick Start
```bash
# Start all services
docker-compose up -d

# Start only database
docker-compose up -d postgres

# Run application-service locally
cd application-service
./gradlew bootRun
```

### Environment Variables
Copy `env.example` to `.env` and configure:
- Database credentials
- Service ports
- Spring profiles

## 📊 Monitoring
- Health checks: `/actuator/health`
- Application metrics: `/actuator/metrics`
- Database monitoring: PostgreSQL logs

## 🧪 Testing
```bash
# Run all tests
./gradlew test

# Run specific service tests
./gradlew :application-service:test
```

## 📚 Documentation
- [Main README](./README.md)
- [Architecture Guide](./ARCHITECTURE.md)
- [Commands Reference](./COMMANDS.md)
- [Application Service](./application-service/README.md)
