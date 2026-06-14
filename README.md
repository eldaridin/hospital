# Sistema Hospitalario con Microservicios

Sistema modular de gestión hospitalaria desarrollado con arquitectura de microservicios en **Java Spring Boot**. El proyecto está en fase preliminar y será expandido con funcionalidades adicionales.

## 📋 Tabla de Contenidos

- [Microservicios Implementados](#microservicios-implementados)
- [Puertos y Configuración](#puertos-y-configuración)
- [Rutas Principales del Gateway](#rutas-principales-del-gateway)
- [Documentación Swagger](#documentación-swagger)
- [Instrucciones de Ejecución](#instrucciones-de-ejecución)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)

## 🏥 Microservicios Implementados

| Servicio | Descripción | Puerto | Tecnología |
|----------|-------------|--------|-----------|
| **Patient Service** | Gestión de pacientes | 8081 | Spring Boot 4.0.6 |
| **Appointment Service** | Gestión de citas médicas | 8082 | Spring Boot 4.0.6 |
| **Doctor Service** | Gestión de médicos | 8083 | Spring Boot 4.0.6 |
| **Auth Service** | Autenticación y autorización con JWT | 8084 | Spring Boot 4.0.6 |
| **Prescription Service** | Gestión de prescripciones | 8085 | Spring Boot 3.3.0 |
| **Inventory Service** | Gestión de inventario/recursos | 8086 | Spring Boot 4.1.0 |

### Características por Servicio

- **Patient Service**: Administración de datos de pacientes
- **Appointment Service**: Reserva y gestión de citas con integración de OpenFeign
- **Doctor Service**: Base de datos de médicos y especialidades
- **Auth Service**: Control de acceso con autenticación JWT
  - Secret: `ClaveHospital123`
  - Expiración: 1 hora
- **Prescription Service**: Gestión de prescripciones médicas
- **Inventory Service**: Control de inventario hospitalario

## 🔌 Puertos y Configuración

```
Patient Service       → http://localhost:8081
Appointment Service   → http://localhost:8082
Doctor Service        → http://localhost:8083
Auth Service          → http://localhost:8084
Prescription Service  → http://localhost:8085
Inventory Service     → http://localhost:8086
```

### Base de Datos

Cada servicio tiene su propia base de datos MySQL:

```
patient-service    → patient_db
appointment-service → appointment_db
doctor-service     → doctor_db
auth-service       → auth_db
prescription-service → db_prescriptions
inventory-service  → db_inventario
```

**Configuración por defecto:**
- Usuario: `root`
- Contraseña: (vacía)
- Host: `localhost:3306`

## 🚀 Rutas Principales del Gateway

*En desarrollo. Las rutas específicas de cada servicio estarán disponibles cuando se implemente el API Gateway.*

Próximamente se añadirá un API Gateway centralizado con rutas consolidadas para todos los microservicios.

## 📚 Documentación Swagger

*Las documentaciones de Swagger estarán disponibles localmente una vez se añadan las dependencias de SpringDoc OpenAPI a cada servicio.*

Rutas esperadas (cuando se implemente):
- Patient Service: `http://localhost:8081/swagger-ui.html`
- Appointment Service: `http://localhost:8082/swagger-ui.html`
- Doctor Service: `http://localhost:8083/swagger-ui.html`
- Auth Service: `http://localhost:8084/swagger-ui.html`
- Prescription Service: `http://localhost:8085/swagger-ui.html`
- Inventory Service: `http://localhost:8086/swagger-ui.html`

## 🛠️ Instrucciones de Ejecución

### Requisitos Previos

- **Java JDK 21** o superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Ejecución Local

#### 1. Clonar el Repositorio

```bash
git clone https://github.com/eldaridin/hospital.git
cd hospital
```

#### 2. Crear Bases de Datos

```sql
-- Conectarse a MySQL y ejecutar:
CREATE DATABASE IF NOT EXISTS patient_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS appointment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS doctor_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_prescriptions CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. Compilar Microservicios

```bash
# Compilar todos los servicios
mvn clean install

# O compilar individualmente
cd patient-service && mvn clean install && cd ..
cd appointment-service && mvn clean install && cd ..
cd doctor-service && mvn clean install && cd ..
cd auth-service && mvn clean install && cd ..
cd prescription-service && mvn clean install && cd ..
cd inventory-service && mvn clean install && cd ..
```

#### 4. Ejecutar Microservicios

**Opción A: Con Maven**

```bash
# Terminal 1 - Patient Service
cd patient-service && mvn spring-boot:run

# Terminal 2 - Appointment Service
cd appointment-service && mvn spring-boot:run

# Terminal 3 - Doctor Service
cd doctor-service && mvn spring-boot:run

# Terminal 4 - Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 5 - Prescription Service
cd prescription-service && mvn spring-boot:run

# Terminal 6 - Inventory Service
cd inventory-service && mvn spring-boot:run
```

**Opción B: Con JAR compilados**

```bash
java -jar patient-service/target/patient-service-0.0.1-SNAPSHOT.jar
java -jar appointment-service/target/appointment-service-0.0.1-SNAPSHOT.jar
java -jar doctor-service/target/doctor-service-0.0.1-SNAPSHOT.jar
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar
java -jar prescription-service/target/presciption-service-0.0.1-SNAPSHOT.jar
java -jar inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar
```

### Validar Ejecución

Verificar que todos los servicios están activos:

```bash
curl http://localhost:8081/health  # Patient Service
curl http://localhost:8082/health  # Appointment Service
curl http://localhost:8083/health  # Doctor Service
curl http://localhost:8084/health  # Auth Service
curl http://localhost:8085/health  # Prescription Service
curl http://localhost:8086/health  # Inventory Service
```

### Ejecución Remota (Producción)

*Las instrucciones de despliegue remoto (Docker, Kubernetes, Cloud providers) serán añadidas en futuras versiones.*

Próximamente se proporcionará:
- Dockerfiles para cada servicio
- Docker Compose para orquestación local
- Configuración de Kubernetes
- Despliegue en servicios cloud (AWS, Azure, GCP)

## 💻 Tecnologías Utilizadas

### Backend
- **Java 21/26**
- **Spring Boot 4.0.6 / 4.1.0 / 3.3.0**
- **Spring Data JPA**
- **Spring Web MVC**
- **Flyway** - Migraciones de base de datos
- **Kotlin** (en Auth Service)
- **OpenFeign** (en Appointment Service)

### Base de Datos
- **MySQL 8.0+**
- **MySQL Connector/J**
- **Hibernate**

### Testing
- **Spring Boot Test**
- **JUnit**

### Build
- **Maven**

## 📝 Notas

- Este proyecto está en **fase preliminar** y será expandido continuamente.
- Se añadirán más microservicios según sea necesario.
- La documentación será actualizada con nuevas funcionalidades.
- Se implementará un API Gateway centralizado en futuras versiones.

## 👤 Autor

**eldaridin** - GitHub: [@eldaridin](https://github.com/eldaridin)

---

**Última actualización:** Junio 2026
