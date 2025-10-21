# e2e-tests/api_clients.py
import requests

class ApplicationServiceClient:
    def __init__(self, base_url):
        self.base_url = base_url

    def create_application(self, data):
        response = requests.post(f"{self.base_url}/applications", json=data)
        response.raise_for_status()
        return response.json()

    def get_application(self, app_id):
        response = requests.get(f"{self.base_url}/applications/{app_id}")
        response.raise_for_status()
        return response.json()

    def update_application(self, app_id, data):
        response = requests.put(f"{self.base_url}/applications/{app_id}", json=data)
        response.raise_for_status()
        return response.json()

    def delete_application(self, app_id):
        response = requests.delete(f"{self.base_url}/applications/{app_id}")
        response.raise_for_status()

class NotificationServiceClient:
    def __init__(self, base_url):
        self.base_url = base_url

    def get_notifications(self):
        response = requests.get(f"{self.base_url}/notifications")
        response.raise_for_status()
        return response.json()
