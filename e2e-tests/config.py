# e2e-tests/config.py

# Service URLs
API_GATEWAY_URL = "http://localhost:8000"
APPLICATION_SERVICE_URL = "http://localhost:8080"
NOTIFICATION_SERVICE_URL = "http://localhost:8083"

# PostgreSQL Connection Details
POSTGRES_DB_1 = {
    "dbname": "sql4",
    "user": "postgres",
    "password": "password",
    "host": "localhost",
    "port": "5432"
}

POSTGRES_DB_2 = {
    "dbname": "sql7",
    "user": "postgres",
    "password": "password",
    "host": "localhost",
    "port": "5433"
}

# RabbitMQ Connection Details
RABBITMQ_HOST = "localhost"
RABBITMQ_PORT = 5672
RABBITMQ_USER = "guest"
RABBITMQ_PASS = "guest"
