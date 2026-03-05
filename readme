# VantusExpress — Backend API

Backend REST para sistema de paquetería, desarrollado con **Java Spring Boot** y **MariaDB**. Gestiona sesiones de llamadas, clientes, destinatarios, envíos y bitácora de llamadas para la operación de VantusExpress.

---

## Stack tecnológico

- Java 21
- Spring Boot 4.0.3
- Spring Data JPA + Hibernate 7
- MariaDB 11.4
- Gradle

---

## Estructura del proyecto

```
src/main/java/com/vantus/vantusexpress/
├── controller/         # Endpoints REST
├── service/            # Lógica de negocio
├── repository/         # Interfaces JPA
├── entity/             # Entidades de base de datos
└── VantusExpressApplication.java
```

---

## Configuración

Copia y edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/express
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.open-in-view=false

server.port=8080
```

> Las tablas se crean automáticamente al levantar la aplicación con `ddl-auto=update`.

---

## Levantar el proyecto

```bash
./gradlew bootRun
```

La API estará disponible en `http://localhost:8080`.

---

## Endpoints

### Sesiones `/sesion`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/sesion` | Crear sesión |
| GET | `/sesion/{callSid}` | Obtener sesión por CallSid |
| PUT | `/sesion/{callSid}` | Actualizar sesión |
| DELETE | `/sesion/{callSid}` | Eliminar sesión |

**Body POST/PUT:**
```json
{
  "callSid": "CA1234567890",
  "pasoActual": "bienvenida",
  "datos": {
    "intentos": 0,
    "opcion": null
  }
}
```

---

### Clientes `/clientes`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/clientes` | Crear cliente |
| GET | `/clientes?telefono=XXX` | Buscar por teléfono |
| GET | `/clientes/{id}` | Obtener por ID |
| PUT | `/clientes/{id}` | Actualizar cliente |
| DELETE | `/clientes/{id}` | Eliminar cliente |

**Body POST/PUT:**
```json
{
  "nombre": "Victor García",
  "telefono": "2461234567",
  "direccion": "Calle Morelos 12, Apizaco, Tlaxcala"
}
```

---

### Destinatarios `/destinatarios`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/destinatarios?clienteId=1` | Crear destinatario |
| GET | `/destinatarios?clienteId=1` | Listar por cliente |
| GET | `/destinatarios/{id}` | Obtener por ID |
| PUT | `/destinatarios/{id}` | Actualizar destinatario |
| DELETE | `/destinatarios/{id}` | Eliminar destinatario |

**Body POST/PUT:**
```json
{
  "nombre": "Juan Pérez",
  "telefono": "2469876543",
  "direccion": "Av. Hidalgo 45, CDMX"
}
```

---

### Envíos `/envios`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/envios?clienteId=1&destinatarioId=1` | Crear envío |
| GET | `/envios?folio=PX1234567890` | Buscar por folio |
| GET | `/envios/{id}` | Obtener por ID |
| PUT | `/envios/{id}` | Actualizar envío |
| DELETE | `/envios/{id}` | Eliminar envío |

**Body POST:**
```json
{
  "contenido": "Electrónicos",
  "valorEstimado": 1500.00
}
```

> El folio se genera automáticamente con el formato `PX` + timestamp (ej. `PX1741234567890`).

**Body PUT:**
```json
{
  "contenido": "Electrónicos",
  "valorEstimado": 1500.00,
  "estado": "en tránsito"
}
```

---

### Bitácora de llamadas `/bitacora`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/bitacora` | Registrar llamada |

**Body POST:**
```json
{
  "callSid": "CA1234567890",
  "telefono": "2461234567",
  "duracion": 120,
  "resultado": "completado"
}
```

---

## Modelo de base de datos

```
clientes
  └── destinatarios (cliente_id FK)
  └── envios (cliente_id FK)
        └── destinatarios (destinatario_id FK)

sesiones          (independiente, manejo de estado de llamadas)
bitacora_llamadas (independiente, registro de llamadas)
```

---

## Estados de envío

| Estado | Descripción |
|--------|-------------|
| `registrado` | Estado inicial al crear el envío |
| `en tránsito` | Envío en camino |
| `entregado` | Envío recibido por destinatario |
| `cancelado` | Envío cancelado |