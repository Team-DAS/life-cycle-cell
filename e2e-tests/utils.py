# e2e-tests/utils.py
import time
import logging

def retry(attempts=10, delay=10):
    def decorator(func):
        def wrapper(*args, **kwargs):
            for i in range(attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    logging.warning(f"Attempt {i+1}/{attempts} failed for {func.__name__}: {e}")
                    if i < attempts - 1:
                        logging.info(f"Retrying in {delay} seconds...")
                        time.sleep(delay)
            raise Exception(f"Function {func.__name__} failed after {attempts} attempts.")
        return wrapper
    return decorator
