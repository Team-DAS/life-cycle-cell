# Multi-stage build for API Gateway - CORREGIDO
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# Copy ALL necessary gradle files from root
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ gradle/

# Copy api-gateway module specifically
COPY api-gateway/build.gradle api-gateway/
COPY api-gateway/src/ api-gateway/src/

# Install dos2unix and fix line endings
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*
RUN dos2unix gradlew && chmod +x ./gradlew

# Build ONLY the api-gateway module from root project
RUN bash ./gradlew :api-gateway:build -x test

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built jar from CORRECT path - FIXED THIS LINE
COPY --from=builder /app/api-gateway/build/libs/*.jar app.jar

# Expose port
EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8000/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]