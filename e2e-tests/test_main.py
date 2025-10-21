import pytest
import requests
import pika
import psycopg2
import time
import logging
from config import (
    API_GATEWAY_URL,
    APPLICATION_SERVICE_URL,
    NOTIFICATION_SERVICE_URL,
    POSTGRES_DB_1,
    POSTGRES_DB_2,
    RABBITMQ_HOST,
)
from api_clients import ApplicationServiceClient, NotificationServiceClient
from utils import retry

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Fixtures for database connections
@pytest.fixture(scope="module")
def postgres_conn_1():
    db_config = POSTGRES_DB_1.copy()
    db_config['client_encoding'] = 'utf8'
    conn = psycopg2.connect(**db_config)
    yield conn
    conn.close()

@pytest.fixture(scope="module")
def postgres_conn_2():
    db_config = POSTGRES_DB_2.copy()
    db_config['client_encoding'] = 'utf8'
    conn = psycopg2.connect(**db_config)
    yield conn
    conn.close()

# Fixture for RabbitMQ connection
@pytest.fixture(scope="module")
def rabbitmq_conn():
    connection = pika.BlockingConnection(pika.ConnectionParameters(RABBITMQ_HOST))
    yield connection
    connection.close()

# Test Suite
class TestLifeCycleCellE2E:
    # 1. Network Connectivity & Service Health
    @retry(attempts=5, delay=5)
    def test_service_health_checks(self):
        logging.info("Checking service health...")
        assert requests.get(f"{API_GATEWAY_URL}/actuator/health").status_code == 200, "API Gateway is not healthy"
        assert requests.get(f"{APPLICATION_SERVICE_URL}/actuator/health").status_code == 200, "Application Service is not healthy"
        assert requests.get(f"{NOTIFICATION_SERVICE_URL}/actuator/health").status_code == 200, "Notification Service is not healthy"
        logging.info("All services are healthy.")

    # 2. Database Schema Verification
    def test_database_schemas(self, postgres_conn_1, postgres_conn_2):
        logging.info("Verifying database schemas...")
        # Check for 'applications' table in 'sql4'
        cursor1 = postgres_conn_1.cursor()
        cursor1.execute("SELECT to_regclass('public.applications')")
        assert cursor1.fetchone()[0] == 'applications', "Table 'applications' not found in sql4"
        cursor1.close()

        # Check for 'notifications' table in 'sql7'
        cursor2 = postgres_conn_2.cursor()
        cursor2.execute("SELECT to_regclass('public.notifications')")
        assert cursor2.fetchone()[0] == 'notifications', "Table 'notifications' not found in sql7"
        cursor2.close()
        logging.info("Database schemas are correct.")

    # 3. API Gateway Routing
    def test_api_gateway_routing(self):
        logging.info("Testing API Gateway routing...")
        # Route to application-service
        response_app = requests.get(f"{API_GATEWAY_URL}/applications")
        assert response_app.status_code in [200, 404], "Routing to Application Service failed"
        # Route to notification-service
        response_notif = requests.get(f"{API_GATEWAY_URL}/notifications")
        assert response_notif.status_code in [200, 404], "Routing to Notification Service failed"
        logging.info("API Gateway routing is working.")

    # 4. Application Service CRUD and Error Handling
    def test_application_service_crud(self, postgres_conn_1):
        client = ApplicationServiceClient(APPLICATION_SERVICE_URL)

        # Create
        app_data = {"name": "Test App", "description": "A test application."}
        app = client.create_application(app_data)
        assert "id" in app and app["name"] == "Test App"
        app_id = app["id"]

        # Read
        fetched_app = client.get_application(app_id)
        assert fetched_app["name"] == "Test App"

        # Update
        updated_data = {"name": "Updated Test App"}
        updated_app = client.update_application(app_id, updated_data)
        assert updated_app["name"] == "Updated Test App"

        # Delete
        client.delete_application(app_id)

        # Verify deletion
        with pytest.raises(requests.exceptions.HTTPError):
            client.get_application(app_id)

    def test_application_service_validation(self):
        client = ApplicationServiceClient(APPLICATION_SERVICE_URL)
        # Test validation for creating an application with invalid data
        with pytest.raises(requests.exceptions.HTTPError) as excinfo:
            client.create_application({"name": ""}) # Invalid name
        assert excinfo.value.response.status_code == 400

    # 5. RabbitMQ Integration
    def test_rabbitmq_messaging(self, rabbitmq_conn):
        channel = rabbitmq_conn.channel()
        # Declare the exchange (idempotent)
        channel.exchange_declare(exchange='notifications_exchange', exchange_type='fanout', durable=True)

        # Declare the queue (idempotent)
        result = channel.queue_declare(queue='notifications_queue', durable=True)
        queue_name = result.method.queue

        # Bind the queue to the exchange
        channel.queue_bind(exchange='notifications_exchange', queue=queue_name)

        # Publish a test message
        test_message = '{"test": "message from test"}'
        channel.basic_publish(exchange='notifications_exchange', routing_key='', body=test_message)

        logging.info("Published test message to RabbitMQ, waiting to consume...")
        time.sleep(2) # Give a moment for the message to be routed

        # Consume the message to verify
        method_frame, header_frame, body = channel.basic_get(queue=queue_name, auto_ack=False)
        assert body is not None, "Failed to consume message from RabbitMQ queue"
        assert body.decode() == test_message
        channel.basic_ack(method_frame.delivery_tag)
        logging.info("Successfully consumed message from RabbitMQ.")

    # 6. Full End-to-End Flow
    def test_full_e2e_flow(self, postgres_conn_2):
        app_client = ApplicationServiceClient(APPLICATION_SERVICE_URL)

        # Create an application
        app_data = {"name": "E2E Flow App", "description": "Testing full flow."}
        app = app_client.create_application(app_data)

        # The creation of an application should trigger a notification.
        # We'll wait a bit for the asynchronous process to complete.
        time.sleep(10)

        # Check the notifications database for a new entry
        cursor = postgres_conn_2.cursor()
        cursor.execute("SELECT * FROM notifications WHERE message LIKE '%E2E Flow App%'")
        notification = cursor.fetchone()
        assert notification is not None, "Notification was not created after application creation"
        cursor.close()

if __name__ == "__main__":
    pytest.main(['-v', '--html=report.html'])
