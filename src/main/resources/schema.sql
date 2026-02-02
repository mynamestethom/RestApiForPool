DROP TABLE IF EXISTS registration;
DROP TABLE IF EXISTS work_time;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE work_time(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week INT NOT NULL CHECK(day_of_week > 0 AND day_of_week < 8) UNIQUE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE reservations(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    registration_date DATE NOT NULL,
    registration_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (client_id, registration_date, registration_time),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT chk_slot CHECK (
        MINUTE(registration_time) = 0 AND SECOND(registration_time) = 0
    )
);