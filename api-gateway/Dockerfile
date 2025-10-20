# Multi-stage build for API Gateway
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# Copy gradle files
COPY gradle/ gradle/
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle .
COPY api-gateway/build.gradle .

# Install dos2unix
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# Copy source code
COPY api-gateway/src/ src/
RUN dos2unix gradlew && chmod +x ./gradlew
# Build the application
RUN bash ./gradlew build -x test

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8000/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
