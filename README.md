# 🏥 Sistema Hospitalario con Microservicios

Sistema modular y escalable de gestión hospitalaria desarrollado con **arquitectura de microservicios** en **Java Spring Boot**. Implementa patrones de comunicación inter-servicios, autenticación JWT y persistencia de datos distribuida.

---

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Microservicios Implementados](#microservicios-implementados)
- [Puertos y Configuración](#puertos-y-configuración)
- [Documentación API](#documentación-api)
- [Instrucciones de Ejecución](#instrucciones-de-ejecución)
- [Despliegue con Docker](#despliegue-con-docker)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Roadmap Futuro](#roadmap-futuro)
- [Contribuciones](#contribuciones)

---

## 📖 Descripción General

Este proyecto implementa un sistema completo de gestión hospitalaria donde cada funcionalidad crítica se ejecuta como un microservicio independiente. Esto permite:

✅ **Escalabilidad**: Cada servicio puede escalar de forma independiente  
✅ **Mantenibilidad**: Equipos pueden desarrollar servicios en paralelo  
✅ **Resiliencia**: Un fallo en un servicio no derriba todo el sistema  
✅ **Flexibilidad**: Fácil actualización individual de componentes  

---

## 🏗️ Arquitectura del Proyecto

```
hospital/
├── patient-service/           (Gestión de pacientes)
├── appointment-service/       (Citas médicas con OpenFeign)
├── doctor-service/            (Base de datos de médicos)
├── auth-service/              (Autenticación JWT - Kotlin)
├── prescription-service/      (Gestión de prescripciones)
├── inventory-service/         (Control de recursos)
├── api-gateway/               (En desarrollo)
├── docker-compose.yml
├── pom.xml                    (Padre)
└── README.md
```

**Composición del código:**
- **Java**: 94.7%
- **Docker**: 5.3%

---

## 🏥 Microservicios Implementados

| Servicio | Descripción | Puerto | Base de Datos | Spring Boot |
|----------|-------------|--------|---------------|------------|
| **Patient Service** | Administración de datos de pacientes | 8081 | patient_db | 4.0.6 |
| **Appointment Service** | Reserva y gestión de citas médicas | 8082 | appointment_db | 4.0.6 |
| **Doctor Service** | Base de datos de médicos y especialidades | 8083 | doctor_db | 4.0.6 |
| **Auth Service** | Autenticación JWT y autorización | 8084 | auth_db | 4.0.6 |
| **Prescription Service** | Gestión de prescripciones médicas | 8085 | db_prescriptions | 3.3.0 |
| **Inventory Service** | Control de inventario hospitalario | 8086 | db_inventario | 4.1.0 |

### 📌 Características Destacadas por Servicio

#### Patient Service
- CRUD completo de pacientes
- Registro de historial médico
- Gestión de contactos de emergencia

#### Appointment Service
- Reserva de citas médicas
- Integración con **OpenFeign** para comunicación inter-servicios
- Validación automática de disponibilidad de médicos

#### Doctor Service
- Registro de médicos y especialidades
- Asignación de horarios
- Gestión de disponibilidad

#### Auth Service (Kotlin)
- Autenticación basada en JWT
- **Secret**: `ClaveHospital123`
- **Expiración de token**: 1 hora
- Control de acceso por roles

#### Prescription Service
- Generación de prescripciones
- Validación de medicamentos
- Historial de prescripciones

#### Inventory Service
- Control de medicamentos y recursos
- Alertas de stock bajo
- Registro de salidas/entradas

---

## 🔌 Puertos y Configuración

### Acceso Local a Servicios

```
🔗 Patient Service       → http://localhost:8081
🔗 Appointment Service   → http://localhost:8082
🔗 Doctor Service        → http://localhost:8083
🔗 Auth Service          → http://localhost:8084
🔗 Prescription Service  → http://localhost:8085
🔗 Inventory Service     → http://localhost:8086
```

### ⚙️ Bases de Datos MySQL

Cada servicio utiliza su propia base de datos:

| Servicio | BD | Puerto |
|----------|--------|--------|
| patient-service | patient_db | 3306 |
| appointment-service | appointment_db | 3306 |
| doctor-service | doctor_db | 3306 |
| auth-service | auth_db | 3306 |
| prescription-service | db_prescriptions | 3306 |
| inventory-service | db_inventario | 3306 |

**Credenciales por defecto:**
```
Usuario: root
Contraseña: (vacía)
Host: localhost:3306
```

---

## 📚 Documentación API

### Swagger UI (Una vez implementado)

Una vez configurado SpringDoc OpenAPI en cada servicio:

```
Patient Service:       http://localhost:8081/swagger-ui.html
Appointment Service:   http://localhost:8082/swagger-ui.html
Doctor Service:        http://localhost:8083/swagger-ui.html
Auth Service:          http://localhost:8084/swagger-ui.html
Prescription Service:  http://localhost:8085/swagger-ui.html
Inventory Service:     http://localhost:8086/swagger-ui.html
```

### Health Check

Verificar salud de servicios:

```bash
curl http://localhost:8081/actuator/health  # Patient Service
curl http://localhost:8082/actuator/health  # Appointment Service
curl http://localhost:8083/actuator/health  # Doctor Service
curl http://localhost:8084/actuator/health  # Auth Service
curl http://localhost:8085/actuator/health  # Prescription Service
curl http://localhost:8086/actuator/health  # Inventory Service
```

---

## 🛠️ Instrucciones de Ejecución

### ✅ Requisitos Previos

```
✓ Java JDK 21 o superior
✓ Maven 3.6+
✓ MySQL 8.0+
✓ Git
✓ Postman o cURL (opcional, para testing)
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
CREATE DATABASE IF NOT EXISTS db_prescriptions CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 🔨 Paso 3: Compilar Proyecto

```bash
# Compilar todos los servicios desde la raíz
mvn clean install

# O compilar individualmente
cd patient-service && mvn clean install && cd ..
cd appointment-service && mvn clean install && cd ..
cd doctor-service && mvn clean install && cd ..
cd auth-service && mvn clean install && cd ..
cd prescription-service && mvn clean install && cd ..
cd inventory-service && mvn clean install && cd ..
```

### ▶️ Paso 4: Ejecutar Microservicios

**Opción A: Usar Maven (6 terminales)**

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

**Opción B: Ejecutar JAR compilados**

```bash
java -jar patient-service/target/patient-service-0.0.1-SNAPSHOT.jar
java -jar appointment-service/target/appointment-service-0.0.1-SNAPSHOT.jar
java -jar doctor-service/target/doctor-service-0.0.1-SNAPSHOT.jar
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar
java -jar prescription-service/target/presciption-service-0.0.1-SNAPSHOT.jar
java -jar inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar
```

### ✔️ Paso 5: Validar Ejecución

```bash
# Verificar que todos los servicios responden
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health

# Resultado esperado: {"status":"UP"}
```

---

## 🐳 Despliegue con Docker

### Construir Imágenes Docker

```bash
# Desde la raíz del proyecto
docker-compose build
```

### Ejecutar con Docker Compose

```bash
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down
```

### Verificar Contenedores

```bash
docker ps
# Deberías ver 6 contenedores en ejecución
```

---

## 💻 Tecnologías Utilizadas

### Backend
- **Java 21/26** - Lenguaje principal
- **Spring Boot 4.0.6 / 4.1.0 / 3.3.0** - Framework principal
- **Spring Data JPA** - ORM y persistencia
- **Spring Web MVC** - REST APIs
- **Spring Security** - Autenticación
- **Kotlin** - Auth Service
- **OpenFeign** - Comunicación inter-servicios
- **Flyway** - Migraciones de BD

### Bases de Datos
- **MySQL 8.0+** - RDBMS principal
- **MySQL Connector/J** - Driver JDBC
- **Hibernate** - ORM

### Testing
- **JUnit 5** - Framework de testing
- **Spring Boot Test** - Testing de Spring
- **Mockito** (Próximamente)

### DevOps
- **Maven** - Gestión de dependencias y build
- **Docker** - Containerización
- **Docker Compose** - Orquestación local

### Patrones de Diseño
- **Microservicios**
- **REST API**
- **JWT (JSON Web Tokens)**
- **Circuit Breaker** (A implementar)
- **API Gateway** (En desarrollo)

---

## 🗺️ Roadmap Futuro

### Fase 2
- [ ] Implementar API Gateway centralizado
- [ ] Agregar documentación Swagger/OpenAPI
- [ ] Implementar Circuit Breaker (Resilience4j)
- [ ] Agregar tests unitarios y de integración

### Fase 3
- [ ] Implementar Message Queue (RabbitMQ/Kafka)
- [ ] Agregar logging centralizado (ELK Stack)
- [ ] Implementar monitoreo con Prometheus
- [ ] Configurar Kubernetes

### Fase 4
- [ ] Despliegue en AWS/Azure/GCP
- [ ] Implementar CQRS pattern
- [ ] Agregar Event Sourcing
- [ ] Frontend web/móvil

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. **Fork** el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregado: nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abrir un **Pull Request**

---

## 📝 Licencia

Este proyecto está en desarrollo y actualmente sin licencia específica.

---

## 👤 Autor

**eldaridin** - GitHub: [@eldaridin](https://github.com/eldaridin)

---

## 📞 Soporte

Para reportar bugs o solicitar features, abre un **Issue** en el repositorio.

---

**Última actualización:** Junio 2026  
**Estado del Proyecto:** En desarrollo activo 🚀
