
# 🧠 Prueba Técnica Backend — CleverTap Integration (Spring Boot + JWT + Swagger)

Este proyecto implementa una API REST en **Spring Boot** que permite:

- Registro y autenticación con **JWT**
- Creación de usuarios y eventos
- Persistencia en **MySQL**
- Sincronización automática con **CleverTap**
- Documentación interactiva con **Swagger / OpenAPI**
- Tests unitarios con **JUnit + Mockito**

---

## 🏗️ Arquitectura

```
Controller → Service → Repository → MySQL
                 ↓
             CleverTap API
```

La seguridad está basada en **JWT stateless** y Swagger está configurado para permitir autenticación con Bearer Token.

---

## ⚙️ Requisitos

- Java 17+
- Maven
- MySQL corriendo en `localhost:3306`
- Cuenta de CleverTap (Sandbox es suficiente)

---

## 🗄️ Configuración de Base de Datos

Crear la base:

```sql
CREATE DATABASE clevertap_demo;
```

---

## 🔐 Variables de configuración

Editar `src/main/resources/application-local.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clevertap_demo
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

security.jwt.secret=CLAVE_DE_32_CARACTERES_MINIMO
security.jwt.expiration-ms=3600000

clevertap.accountId=TU_ACCOUNT_ID
clevertap.passcode=TU_PASSCODE
clevertap.region=eu1
clevertap.enabled=true
```

---

## ▶️ Ejecutar el proyecto

```bash
mvn spring-boot:run
```

Servidor en:

```
http://localhost:8080
```

---

## 📘 Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

Aquí puedes probar toda la API sin Postman.

---

## 🔁 Flujo correcto para probar la API

### 1️⃣ Registrar usuario para login

`POST /auth/register`

```json
{
  "email": "prueba@test.com",
  "password": "12345678"
}
```

---

### 2️⃣ Login para obtener JWT

`POST /auth/login`

Respuesta:

```json
{
  "token": "...",
  "expiresInSeconds": 3600
}
```

---

### 3️⃣ Autorizar Swagger

- Click en **Authorize**
- Pegar el token
- Ahora Swagger enviará automáticamente el header:

```
Authorization: Bearer <token>
```

---

### 4️⃣ Crear usuario de negocio

`POST /users`

```json
{
  "externalId": "user-100",
  "name": "Erick Correcto",
  "email": "test@test.com"
}
```

Este usuario se guarda en MySQL **y se envía a CleverTap como Profile**.

---

### 5️⃣ Crear evento

`POST /events`

```json
{
  "userExternalId": "user-100",
  "eventName": "PURCHASE"
}
```

Este evento se guarda en MySQL **y se envía a CleverTap como Event**.

---

## 📊 Verificación en CleverTap

Ir a:

- **Find People → buscar externalId**
- Verás:
  - Profile creado
  - Evento registrado

---

## 🧪 Tests

Ejecutar:

```bash
mvn test
```

Incluye:

- JwtServiceTest
- UserServiceTest
- EventServiceTest

---

## 🛡️ Seguridad

- JWT Stateless
- Filtro `JwtAuthFilter`
- Swagger permitido sin token, pero endpoints protegidos
- Passwords encriptados con BCrypt

---

## 🧩 Endpoints

| Método | Endpoint | Descripción |
|-------|----------|-------------|
| POST | /auth/register | Registrar usuario login |
| POST | /auth/login | Obtener JWT |
| POST | /users | Crear usuario negocio |
| GET | /users/{externalId}/events | Consultar eventos |
| POST | /events | Crear evento |
| GET | /ping | Healthcheck |

---

## 📸 Screenshots de la prueba funcionando

### 🔹 Swagger UI
![Swagger](docs/images/swagger.png)

### 🔹 Login y obtención de JWT
![Login Token](docs/images/login-token.png)

### 🔹 Autorización en Swagger con Bearer Token
![Authorize](docs/images/swagger-authorize.png)

### 🔹 Creación de usuario (200 OK)
![Create User](docs/images/create-user.png)

### 🔹 Creación de evento (200 OK)
![Create Event](docs/images/create-event.png)

### 🔹 Perfil creado en CleverTap
![CleverTap Profile](docs/images/clevertap-profile.png)

### 🔹 Evento registrado en CleverTap (Activity)
![CleverTap Activity](docs/images/clevertap-activity.png)

---

## 💡 Notas técnicas importantes

- El orden correcto es: **register → login → authorize → users → events**
- Si `/users` da 401/403 es porque Swagger no tiene el token
- Si Swagger no carga, revisar que `/v3/api-docs` esté permitido en SecurityConfig

---

## 👨‍💻 Autor

Erick Anaya
