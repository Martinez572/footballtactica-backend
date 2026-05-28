# FootballTacticIA - Backend

REST API desarrollada con Spring Boot para la plataforma de analisis tactico de futbol con inteligencia artificial.

---

## Tecnologias y versiones

| Tecnologia | Version |
|---|---|
| Java | 21.0.10 (LTS) |
| Spring Boot | 3.5.11 |
| Maven | 3.9.6 |
| PostgreSQL Driver | 42.7.10 |
| Hibernate ORM | 6.6.42.Final |
| HikariCP | 6.3.3 |
| Spring Security | 6.5.8 |
| JJWT | 0.11.5 |
| Lombok | 1.18.x |
| Spring Data JPA | 3.5.9 |
| Tomcat embebido | 10.1.52 |

---

## Requisitos previos

- Java 21 o superior
- Maven (incluido via mvnw)
- Cuenta en Supabase con PostgreSQL activo
- API key de Google Gemini

---

## Estructura del proyecto
src/main/java/com/footballtactica/backend/
├── constants/
│   └── ApiConstants.java           # Rutas de la API versionadas en /api/v1/
├── controller/
│   ├── AuthController.java         # Registro e inicio de sesion
│   ├── PlayerController.java       # CRUD jugadores
│   ├── TacticController.java       # CRUD tacticas con datos JSONB
│   ├── PlayController.java         # CRUD jugadas
│   └── AIReportController.java     # Endpoints de analisis con Gemini
├── dto/
│   ├── AuthRequest.java            # Datos de entrada para autenticacion
│   └── AuthResponse.java           # Token JWT + datos del usuario
├── entity/
│   ├── User.java                   # Tabla usuarios
│   ├── Player.java                 # Tabla jugadores
│   ├── Tactic.java                 # Tabla tacticas (campo datos JSONB)
│   ├── TacticState.java            # Enum ATTACK, DEFENSE, SET_PIECE, KICKOFF
│   ├── Play.java                   # Tabla jugadas (campo datos JSONB)
│   └── AIReport.java               # Tabla reportes_ia
├── repository/
│   ├── UserRepository.java
│   ├── PlayerRepository.java
│   ├── TacticRepository.java
│   ├── PlayRepository.java
│   └── AIReportRepository.java
├── security/
│   ├── JwtUtil.java                # Generacion y validacion de tokens
│   ├── JwtFilter.java              # Filtro HTTP que intercepta cada request
│   └── SecurityConfig.java         # Configuracion de Spring Security y CORS
└── service/
├── AuthService.java            # Logica de registro y login con BCrypt
├── PlayerService.java
├── TacticService.java
├── PlayService.java
├── GeminiService.java          # Llamadas a la API de Google Gemini
└── AIReportService.java        # Generacion y persistencia de reportes IA

---

## Variables de entorno

El archivo `application.properties` no se sube a GitHub. Debe crearse manualmente en `src/main/resources/` con el siguiente contenido:

```properties
spring.application.name=FootballTacticIA
spring.datasource.url=jdbc:postgresql://HOST:5432/postgres
spring.datasource.username=postgres.TU_PROJECT_REF
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.datasource.hikari.maximum-pool-size=2
spring.datasource.hikari.minimum-idle=1
jwt.secret=clave_secreta_minimo_32_caracteres
jwt.expiration=86400000
gemini.api.key=TU_API_KEY_DE_GOOGLE_GEMINI
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
server.port=8080
```

Para despliegue en Render o Railway, las mismas propiedades se configuran como variables de entorno usando el formato `SPRING_DATASOURCE_URL`, `JWT_SECRET`, etc.

---

## Correr en local

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

El servidor inicia en `http://localhost:8080`

Para limpiar y recompilar desde cero:

```bash
mvnw.cmd clean spring-boot:run
```

---

## Correr con Docker

```bash
# Construir imagen
docker build -t footballtactica-backend .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://... \
  -e SPRING_DATASOURCE_USERNAME=... \
  -e SPRING_DATASOURCE_PASSWORD=... \
  -e JWT_SECRET=... \
  -e GEMINI_API_KEY=... \
  footballtactica-backend
```

---

## Endpoints

Todos los endpoints usan el prefijo `/api/v1/`

### Autenticacion (publica)

| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | /api/v1/auth/register | Crear cuenta nueva |
| POST | /api/v1/auth/login | Iniciar sesion y obtener token |

### Jugadores (requiere JWT)

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | /api/v1/players/user/{userId} | Listar jugadores del usuario |
| POST | /api/v1/players | Crear jugador |
| DELETE | /api/v1/players/{id} | Eliminar jugador |

### Tacticas (requiere JWT)

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | /api/v1/tactics/user/{userId} | Listar tacticas del usuario |
| POST | /api/v1/tactics | Crear tactica |
| PUT | /api/v1/tactics/{id} | Actualizar tactica |
| DELETE | /api/v1/tactics/{id} | Eliminar tactica |

### Jugadas (requiere JWT)

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | /api/v1/plays/tactic/{tacticId} | Listar jugadas de una tactica |
| POST | /api/v1/plays | Crear jugada |
| DELETE | /api/v1/plays/{id} | Eliminar jugada |

### Reportes IA (requiere JWT)

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | /api/v1/reports/user/{userId} | Listar reportes del usuario |
| POST | /api/v1/reports/player/{userId}/{playerId} | Analisis de jugador |
| POST | /api/v1/reports/tactic/{userId} | Analisis de tactica |
| POST | /api/v1/reports/video-file/{userId} | Analisis de video subido |
| POST | /api/v1/reports/comparison/{userId} | Comparativa entre jugadores |

Para endpoints protegidos incluir en el header:
Authorization: Bearer <token>

---

## Base de datos

PostgreSQL en Supabase (plan gratuito, region Sao Paulo).

| Tabla | Descripcion |
|---|---|
| usuarios | Cuentas de usuario con password_hash BCrypt |
| jugadores | Plantilla de jugadores por usuario |
| tacticas | Tacticas con campo datos JSONB (posiciones y rutas animadas) |
| posiciones_tactica | Posiciones de jugadores en tacticas |
| jugadas | Biblioteca de jugadas por tactica |
| reportes_ia | Reportes generados por Gemini AI |

Los campos `datos` de tipo JSONB requieren la siguiente anotacion en la entidad:

```java
@org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
@Column(name = "datos", columnDefinition = "jsonb")
private String data;
```

El plan gratuito de Supabase limita las conexiones simultaneas. Por eso HikariCP se configura con maxima de 2 conexiones.

---

## Patrones de diseno implementados

| Patron | Donde se aplica |
|---|---|
| Builder | Player.java, Tactic.java, Play.java, AIReport.java, User.java |
| State | TacticState.java (ATTACK, DEFENSE, SET_PIECE, KICKOFF) |
| Singleton | HikariCP connection pool |
| Facade | GeminiService.java encapsula toda la logica de Gemini API |
| Repository | Todos los archivos *Repository.java |
| DTO | AuthRequest.java y AuthResponse.java |
| Filter | JwtFilter.java intercepta y valida tokens en cada request |

---

## Despliegue en produccion

El backend se despliega en Render usando Docker.

El `Dockerfile` en la raiz del proyecto realiza una construccion en dos etapas:
1. Etapa de compilacion con Maven y JDK 21
2. Etapa de ejecucion con JRE 21 (imagen mas liviana)

URL de produccion: `https://footballtactica-backend.onrender.com`

El plan gratuito de Render suspende el servicio tras 15 minutos de inactividad. El primer request despues de la suspension puede tardar hasta 50 segundos.