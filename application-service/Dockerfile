# Multi-stage build for Application Service
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

# Install required packages
RUN apt-get update && apt-get install -y bash dos2unix

# Copy the entire project structure
COPY . .

# Convert line endings and set permissions
RUN dos2unix gradlew && \
    chmod +x gradlew && \
    echo "Current directory:" && pwd && \
    echo "Project structure:" && find . -type d

# Verify Gradle version and download dependencies
RUN bash ./gradlew --version && \
    bash ./gradlew dependencies

# Build the specific project
RUN bash ./gradlew :application-service:build -x test

# Runtime stage
FROM openjdk:17-slim

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built jar from builder stage
COPY --from=builder /app/application-service/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
