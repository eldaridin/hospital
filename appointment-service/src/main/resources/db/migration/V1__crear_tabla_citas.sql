CREATE TABLE citas (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       paciente_id BIGINT NOT NULL,
                       doctor_id BIGINT NOT NULL,
                       fecha DATE NOT NULL,
                       hora TIME NOT NULL,
                       motivo VARCHAR(255) NOT NULL,
                       estado VARCHAR(50) DEFAULT 'PENDIENTE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);