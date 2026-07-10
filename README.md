# 🏥 Sistema Hospitalario con Microservicios

Sistema modular y escalable de gestión hospitalaria desarrollado con **arquitectura de microservicios** en **Java Spring Boot**. Implementa patrones de comunicación inter-servicios, autenticación JWT y orquestación con Docker.

---

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Microservicios Implementados](#microservicios-implementados)
- [Componentes de Infraestructura](#componentes-de-infraestructura)
- [Puertos y Configuración](#puertos-y-configuración)
- [Documentación API](#documentación-api)
- [Instrucciones de Ejecución Local](#instrucciones-de-ejecución-local)
- [Despliegue con Docker](#despliegue-con-docker)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Estructura de Base de Datos](#estructura-de-base-de-datos)
- [Comunicación Inter-Servicios](#comunicación-inter-servicios)
- [Roadmap Futuro](#roadmap-futuro)
- [Contribuciones](#contribuciones)
- [Soporte](#soporte)

---

## 📖 Descripción General

Este proyecto implementa un sistema completo de gestión hospitalaria donde cada funcionalidad crítica se ejecuta como un microservicio independiente. La arquitectura permite:

✅ **Escalabilidad**: Cada servicio escala de forma independiente  
✅ **Mantenibilidad**: Equipos pueden desarrollar servicios en paralelo  
✅ **Resiliencia**: Un fallo en un servicio no derriba todo el sistema  
✅ **Flexibilidad**: Fácil actualización individual de componentes  
✅ **Alta disponibilidad**: Service discovery automático con Eureka  
✅ **Comunicación asíncrona**: Integración con RabbitMQ para eventos  

---

## 🏗️ Arquitectura del Proyecto

### Diagrama de Componentes

```
┌────────────────────────────────────────────────────────────────────┐
│                        CLIENTE (Frontend/Mobile)                     │
└────────────────────────┬───────────────────────────────────────────┘
                         │
                ┌────────▼────────┐
                │  API GATEWAY    │ (Puerto 8080)
                │  Spring Cloud   │
                │   Gateway       │
                └────────┬────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        │        ┌───────┴──────────┐     │
        │        │   Eureka Server  │     │
        │        │   (Service Disc.)│     │
        │        │   (Puerto 8761)  │     │
        │        └──────────────────┘     │
        │                                  │
        ▼                                  ▼
    ┌─────────────────┐        ┌──────────────────────┐
    │ Patient Service │        │ Appointment Service  │
    │   (8081)        │        │      (8082)          │
    └─────────────────┘        └──────────────────────┘
            │                           │
            ▼                           ▼
    ┌─────────────────┐        ┌──────────────────┐
    │ Doctor Service  │        │  Auth Service    │
    │   (8083)        │        │  JWT (Kotlin)    │
    └─────────────────┘        │  (8084)          │
                               └──────────────────┘
        ┌──────────────────────────────────────┬─────────────────┐
        │                                      │                 │
        ▼                                      ▼                 ▼
    ┌──────────────────┐    ┌───────────────┐ ┌──────────────────┐
    │ Prescription     │    │   Inventory   │ │  Pharmacy Service│
    │ Service (8085)   │    │ Service (8086)│ │  (8087)          │
    └──────────────────┘    └───────────────┘ └──────────────────┘
             │                                       │
             ▼                                       ▼
    ┌──────────────────────┐          ┌──────────────────────────┐
    │ Hospitalization      │          │ Notification Service     │
    │ Service (8090)       │          │ (RabbitMQ) (8088)        │
    └──────────────────────┘          └──────────────────────────┘
            │                              │
            └──────────────────┬───────────┘
                               ▼
                      ┌──────────────────┐
                      │  Billing Service │
                      │  (8089)          │
                      └──────────────────┘

┌────────────────────────────────────────────────────────────────────┐
│                      INFRAESTRUCTURA COMPARTIDA                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ MySQL    │  │ RabbitMQ │  │  Redis   │  │ Eureka   │             │
│  │  (3306)  │  │ (5672)   │  │ (6379)   │  │ (8761)   │             │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘             │
└────────────────────────────────────────────────────────────────────┘
```

### Estructura de Carpetas

```
hospital/
├── api-gateway/                    # Spring Cloud Gateway (8080)
├── patient-service/                # Gestión de pacientes (8081)
├── appointment-service/            # Citas médicas (8082)
├── doctor-service/                 # Base de médicos (8083)
├── auth-service/                   # Autenticación JWT (8084)
├── prescription-service/           # Prescripciones (8085)
├── inventory-service/              # Inventario (8086)
├── pharmacy-service/               # Farmacia (8087)
├── notification-service/           # Notificaciones (8088)
├── billing-service/                # Facturación (8089)
├── hospitalization-service/        # Hospitalización (8090)
├── eureka-server/                  # Service Discovery
├── docker-compose.yml              # Orquestación local
├── init.sql                        # Scripts de BD
├── pom.xml                         # Padre multi-módulo
└── README.md                       # Este archivo
```

**Composición del código:**
- **Java**: 97.6%
- **Dockerfile**: 2.4%

---

## 🏥 Microservicios Implementados

| Servicio | Descripción | Puerto | Base de Datos | Spring Boot | Estado |
|----------|-------------|--------|---------------|------------|--------|
| **API Gateway** | Enrutador centralizado | 8080 | N/A | 3.2.5 | ✅ Active |
| **Patient Service** | Administración de pacientes | 8081 | patient_db | 3.2.5 | ✅ Active |
| **Appointment Service** | Reserva de citas médicas | 8082 | appointment_db | 3.2.5 | ✅ Active |
| **Doctor Service** | Base de datos de médicos | 8083 | doctor_db | 3.2.5 | ✅ Active |
| **Auth Service** | Autenticación JWT | 8084 | auth_db | 3.2.5 | ✅ Active |
| **Prescription Service** | Gestión de prescripciones | 8085 | prescription_db | 3.2.5 | ✅ Active |
| **Inventory Service** | Control de inventario | 8086 | inventory_db | 3.2.5 | ✅ Active |
| **Pharmacy Service** | Gestión de farmacia | 8087 | pharmacy_db | 3.2.5 | ✅ Active |
| **Notification Service** | Notificaciones (RabbitMQ) | 8088 | N/A | 3.2.5 | ✅ Active |
| **Billing Service** | Facturación | 8089 | billing_db | 3.2.5 | ✅ Active |
| **Hospitalization Service** | Gestión de hospitalización | 8090 | hospitalization_db | 3.2.5 | ✅ Active |

### 📌 Características Destacadas por Servicio

#### 🏥 Patient Service
- CRUD completo de pacientes
- Registro de historial médico
- Gestión de contactos de emergencia
- Búsqueda y filtrado avanzado

#### 📅 Appointment Service
- Reserva y cancelación de citas
- Integración con **OpenFeign** para comunicación con Patient y Doctor Service
- Validación automática de disponibilidad
- Historial de citas

#### 👨‍⚕️ Doctor Service
- Registro de médicos y especialidades
- Asignación de horarios y disponibilidad
- Calificaciones y experiencia
- Gestión de departamentos

#### 🔐 Auth Service (Kotlin)
- Autenticación basada en JWT
- **Secret**: `ClaveHospital123`
- **Expiración de token**: 1 hora
- Control de acceso por roles (ADMIN, DOCTOR, PATIENT)
- Registro y login de usuarios

#### 💊 Prescription Service
- Generación de prescripciones médicas
- Validación de medicamentos
- Historial de prescripciones
- Integración con Pharmacy Service

#### 📦 Inventory Service
- Control de medicamentos y recursos
- Alertas de stock bajo
- Registro de entrada/salida
- Seguimiento de caducidad

#### 💼 Pharmacy Service
- Gestión de inventario farmacéutico
- Procesamiento de recetas
- Control de dosis
- Reportes de dispensación

#### 📢 Notification Service
- Notificaciones vía RabbitMQ
- Emails a pacientes y médicos
- Recordatorios de citas
- Alertas de sistema

#### 💳 Billing Service
- Generación de facturas
- Cálculo de costos médicos
- Historial de pagos
- Reportes financieros

#### 🛏️ Hospitalization Service
- Registro de hospitalizaciones
- Asignación de camas
- Seguimiento de pacientes
- Alta médica

---

## 🔧 Componentes de Infraestructura

### Eureka Server (Service Discovery)
```
URL: http://localhost:8761
- Registro automático de servicios
- Load balancing
- Health checking
- Descubrimiento dinámico
```

### MySQL Database
```
Container: hospital_mysql
Port: 3306
Credenciales:
  - Usuario: root
  - Contraseña: rootpass
Volumen: mysql_data (persistencia)
```

### RabbitMQ (Message Broker)
```
AMQP Port: 5672
Management Console: http://localhost:15672
Usuario: guest
Contraseña: guest
Funcionalidades:
- Notificaciones asincrónicas
- Event publishing
- Message queues
```

### Redis (Cache)
```
Port: 6379
Funcionalidades:
- Caching de sesiones
- Cache de datos frecuentes
- Operaciones en tiempo real
Volumen: redis_data (persistencia)
```

---

## 🔌 Puertos y Configuración

### Acceso Local a Servicios

```
🌐 API Gateway              → http://localhost:8080
🏥 Patient Service          → http://localhost:8081
📅 Appointment Service      → http://localhost:8082
👨‍⚕️ Doctor Service           → http://localhost:8083
🔐 Auth Service             → http://localhost:8084
💊 Prescription Service     → http://localhost:8085
📦 Inventory Service        → http://localhost:8086
💼 Pharmacy Service         → http://localhost:8087
📢 Notification Service     → http://localhost:8088
💳 Billing Service          → http://localhost:8089
🛏️ Hospitalization Service  → http://localhost:8090
🔍 Eureka Server            → http://localhost:8761
```

### Acceso a Infraestructura

```
🗄️ MySQL                    → localhost:3306
📬 RabbitMQ AMQP           → localhost:5672
📬 RabbitMQ Management     → http://localhost:15672
⚡ Redis                     → localhost:6379
```

### Configuración de Bases de Datos

Cada servicio utiliza su propia base de datos:

```
patient-service          → patient_db
appointment-service      → appointment_db
doctor-service           → doctor_db
auth-service             → auth_db
prescription-service     → prescription_db
inventory-service        → inventory_db
pharmacy-service         → pharmacy_db
billing-service          → billing_db
hospitalization-service  → hospitalization_db
```

**Credenciales por defecto:**
```
Usuario: root
Contraseña: rootpass
Host: localhost:3306 (Local)
Host: mysql (Docker)
```

---

## 📚 Documentación API

### Health Check Endpoints

```bash
# Verificar salud de servicios
curl http://localhost:8080/actuator/health           # API Gateway
curl http://localhost:8081/actuator/health           # Patient Service
curl http://localhost:8082/actuator/health           # Appointment Service
curl http://localhost:8083/actuator/health           # Doctor Service
curl http://localhost:8084/actuator/health           # Auth Service
curl http://localhost:8085/actuator/health           # Prescription Service
curl http://localhost:8086/actuator/health           # Inventory Service
```

### Swagger UI (A implementar)

Una vez configurado SpringDoc OpenAPI:

```
http://localhost:8080/swagger-ui.html                # Gateway
http://localhost:8081/swagger-ui.html                # Patient
http://localhost:8082/swagger-ui.html                # Appointment
http://localhost:8083/swagger-ui.html                # Doctor
http://localhost:8084/swagger-ui.html                # Auth
http://localhost:8085/swagger-ui.html                # Prescription
http://localhost:8086/swagger-ui.html                # Inventory
```

### Service Registry (Eureka)

```
http://localhost:8761/                               # Dashboard
http://localhost:8761/eureka/apps                    # REST API
```

---

## 🛠️ Instrucciones de Ejecución Local

### ✅ Requisitos Previos

```
✓ Java JDK 21 o superior
✓ Maven 3.6+
✓ MySQL 8.0+
✓ Git
✓ Docker (opcional, para ejecución containerizada)
```

### 📥 Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/eldaridin/hospital.git
cd hospital
```

### 🗄️ Paso 2: Crear Bases de Datos

Conectarse a MySQL y ejecutar:

```sql
CREATE DATABASE IF NOT EXISTS patient_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS appointment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS doctor_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS prescription_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS inventory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS pharmacy_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS billing_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS hospitalization_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 🔨 Paso 3: Compilar Proyecto

```bash
# Compilar todos los servicios
mvn clean install

# O compilar individualmente
cd eureka-server && mvn clean install && cd ..
cd api-gateway && mvn clean install && cd ..
cd patient-service && mvn clean install && cd ..
# ... resto de servicios
```

### ▶️ Paso 4: Ejecutar Microservicios (12 terminales)

**Opción A: Usar Maven**

```bash
# Terminal 1 - Eureka Server (Iniciarlo primero)
cd eureka-server && mvn spring-boot:run

# Terminal 2 - API Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 3 - Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 4 - Patient Service
cd patient-service && mvn spring-boot:run

# Terminal 5 - Doctor Service
cd doctor-service && mvn spring-boot:run

# Terminal 6 - Appointment Service
cd appointment-service && mvn spring-boot:run

# Terminal 7 - Prescription Service
cd prescription-service && mvn spring-boot:run

# Terminal 8 - Inventory Service
cd inventory-service && mvn spring-boot:run

# Terminal 9 - Pharmacy Service
cd pharmacy-service && mvn spring-boot:run

# Terminal 10 - Notification Service
cd notification-service && mvn spring-boot:run

# Terminal 11 - Billing Service
cd billing-service && mvn spring-boot:run

# Terminal 12 - Hospitalization Service
cd hospitalization-service && mvn spring-boot:run
```

### ✔️ Paso 5: Validar Ejecución

```bash
# Verificar Eureka Dashboard
curl http://localhost:8761

# Verificar API Gateway
curl http://localhost:8080/actuator/health

# Verificar servicios registrados en Eureka
curl http://localhost:8761/eureka/apps
```

---

## 🐳 Despliegue con Docker

### Prerrequisitos Docker

- Docker instalado y ejecutándose
- Docker Compose 3.8+

### 🔨 Construir Imágenes Docker

```bash
# Desde la raíz del proyecto
docker-compose build

# Para construir un servicio específico
docker-compose build patient-service
```

### ▶️ Ejecutar con Docker Compose

```bash
# Iniciar todos los servicios
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f patient-service

# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```

### 🔍 Verificar Contenedores

```bash
# Listar contenedores en ejecución
docker ps

# Inspeccionar un contenedor específico
docker inspect hospital_mysql

# Acceder a la shell de un contenedor
docker exec -it patient_service /bin/bash
```

### 📊 Monitoreo de Docker

```bash
# Ver estadísticas en tiempo real
docker stats

# Ver logs del contenedor
docker logs hospital_mysql

# Ver eventos de Docker
docker events
```

---

## 💻 Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje principal
- **Spring Boot 3.2.5** - Framework principal
- **Spring Cloud** - Distributed systems toolkit
  - Spring Cloud Gateway - API Gateway
  - Eureka - Service Discovery
  - OpenFeign - Client communication
  - Config Server - Centralized config
- **Spring Data JPA** - ORM y persistencia
- **Spring Web MVC** - REST APIs
- **Spring Security** - Autenticación y autorización
- **JWT 0.12.5** - JWT tokens

### Bases de Datos
- **MySQL 8.0+** - RDBMS principal
- **MySQL Connector/J** - Driver JDBC
- **Hibernate** - ORM
- **Flyway** - Migraciones de base de datos

### Message Broker & Caching
- **RabbitMQ 3.x** - Message broker
- **Redis** - Cache en memoria

### Service Discovery
- **Eureka Server** - Service Registry
- **Eureka Client** - Service Discovery

### Testing
- **JUnit 5** - Framework de testing
- **Spring Boot Test** - Testing de Spring
- **Mockito** - Mocking framework

### DevOps & Deployment
- **Maven 3.6+** - Gestión de dependencias
- **Docker** - Containerización
- **Docker Compose** - Orquestación local

### Patrones de Diseño
- **Microservicios** - Arquitectura
- **REST API** - Protocolo
- **JWT (JSON Web Tokens)** - Autenticación
- **Service Discovery** - Eureka
- **API Gateway** - Enrutamiento centralizado
- **Async Messaging** - RabbitMQ
- **Database per Service** - Persistencia

---

## 🗄️ Estructura de Base de Datos

### Diagrama Relacional Simplificado

```
┌─────────────────────┐     ┌──────────────────┐
│    Patients         │     │    Doctors       │
├─────────────────────┤     ├──────────────────┤
│ id (PK)             │─────│ id (PK)          │
│ name                │     │ name             │
│ email               │     │ specialty        │
│ phone               │     │ schedule         │
│ medical_history     │     │ department       │
└─────────────────────┘     └──────────────────┘
         │                          ▲
         │ 1:N                      │ N:1
         └──────────────────────────┘
              ┌──────────────────┐
              │  Appointments    │
              ├──────────────────┤
              │ id (PK)          │
              │ patient_id (FK)  │
              │ doctor_id (FK)   │
              │ date             │
              │ status           │
              └──────────────────┘
                      │
                      │ 1:N
                      ▼
           ┌─────────────────────────┐
           │   Prescriptions         │
           ├─────────────────────────┤
           │ id (PK)                 │
           │ appointment_id (FK)     │
           │ medication              │
           │ dosage                  │
           │ duration                │
           └─────────────────────────┘
                      │
                      │ 1:N
                      ▼
           ┌─────────────────────────┐
           │  Pharmacy Dispenses     │
           ├─────────────────────────┤
           │ id (PK)                 │
           │ prescription_id (FK)    │
           │ date_dispensed          │
           │ quantity                │
           └─────────────────────────┘









