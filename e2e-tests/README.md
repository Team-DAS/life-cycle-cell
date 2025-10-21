# E2E Tests for Life Cycle Cell

This directory contains the end-to-end test suite for the Life Cycle Cell system.

## Prerequisites
- Python 3.8+
- Docker and Docker Compose

## Setup
1. **Start the services:**
   ```bash
   sudo docker-compose up --build -d
   ```
2. **Install Python dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

## Running the tests
To run the tests, execute the following command from within the `e2e-tests` directory:
```bash
pytest
```

This will run the test suite and generate an HTML report named `report.html`.
