CREATE TABLE doctores (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          telefono VARCHAR(20),
                          especialidad VARCHAR(100) NOT NULL,
                          rut VARCHAR(12) NOT NULL UNIQUE,
                          disponible BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);