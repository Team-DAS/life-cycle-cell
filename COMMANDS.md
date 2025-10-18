# Life Cycle Cell - Development Commands

## 🚀 Quick Start Commands

### Start All Services
```bash
docker-compose up -d
```

### Start Only Database
```bash
docker-compose up -d postgres
```

### Start Application Service Only
```bash
docker-compose up -d postgres application-service
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f application-service
```

### Stop All Services
```bash
docker-compose down
```

### Stop and Remove Volumes
```bash
docker-compose down -v
```

## 🔧 Development Commands

### Build Application Service
```bash
cd application-service
./gradlew build
```

### Run Application Service Locally
```bash
cd application-service
./gradlew bootRun
```

### Run Tests
```bash
# All services
./gradlew test

# Specific service
./gradlew :application-service:test
```

### Clean Build
```bash
./gradlew clean build
```

## 🐳 Docker Commands

### Build Application Service Image
```bash
docker build -t life-cycle-application-service ./application-service
```

### Run Application Service Container
```bash
docker run -p 8080:8080 --name app-service life-cycle-application-service
```

### Access Database
```bash
docker exec -it life-cycle-postgres psql -U postgres -d sql4
```

## 📊 Health Checks

### Check Service Health
```bash
curl http://localhost:8080/actuator/health
```

### Check Database Connection
```bash
docker exec life-cycle-postgres pg_isready -U postgres
```

## 🔍 Debugging

### View Container Logs
```bash
docker logs life-cycle-application-service
```

### Access Container Shell
```bash
docker exec -it life-cycle-application-service /bin/bash
```

### Check Container Status
```bash
docker ps
```

## 🧪 Testing

### Run Integration Tests
```bash
cd application-service
./gradlew integrationTest
```

### Run Tests with Coverage
```bash
cd application-service
./gradlew test jacocoTestReport
```

## 📈 Monitoring

### View Resource Usage
```bash
docker stats
```

### Check Service Dependencies
```bash
docker-compose config
```
