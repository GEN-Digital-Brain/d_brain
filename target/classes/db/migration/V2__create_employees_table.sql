CREATE TABLE employees (
    employee_id BINARY(16) PRIMARY KEY,
    employee_name VARCHAR(255) NOT NULL,
    employee_email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);