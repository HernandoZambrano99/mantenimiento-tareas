# Mantenimiento

API backend de mantenimiento con arquitectura hexagonal

Spring Boot 3.5.14 + Java 21, hexagonal (clean) architecture with JWT auth, Flyway migrations, JaCoCo coverage, and Docker.

## Architecture

```
com.empresa.mantenimiento
├── domain/          # Pure business logic. No Spring imports.
│   ├── constant/    # Per-aggregate message bundles
│   ├── exception/   # DomainNotFound / Validation / Conflict / Unauthorized
│   ├── model/       # Aggregates + their gateway/ output ports
│   └── usecase/     # Use case impls + input/ ports
├── application/handler/  # @Service handlers (thin transactional facade)
└── infrastructure/
    ├── config/                 # BeanConfiguration, JWT, Security, OpenAPI
    ├── drivenadapter/postgresql/  # JPA entities + repos + mappers + adapters
    └── entrypoint/rest/        # Controllers, DTOs, filters
```

The `BeanConfiguration` manually wires each `UseCase` so the domain stays free of Spring annotations.

## Run locally

```bash
# 1. Copy env defaults
cp .env.example .env

# 2. Start PostgreSQL (local docker-compose) — skip this if using Neon
docker compose -f docker/docker-compose.yml up postgres -d

# 3. Build and run
mvn spring-boot:run
```

A bootstrap admin user is created on first launch. If `ADMIN_PASSWORD` is not set, the generated password is logged once — copy it from the startup log.

## Endpoints

- `POST /api/v1/auth/login` — public
- `POST /api/v1/auth/refresh` — public
- `POST /api/v1/auth/change-password` — authenticated
- `/api/v1/users/**` — ADMIN only
- `GET /actuator/health` — public
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`

## Tests + coverage

The integration test spins up a throwaway PostgreSQL via [Testcontainers](https://testcontainers.com/),
so **Docker must be running** — no manual database setup is needed.

```bash
mvn verify
# Report: target/site/jacoco/index.html
```

## Deploy to Render + Neon

`render.yaml` at the repo root is a [Render Blueprint](https://render.com/docs/blueprint-spec). One-click deploy:

1. **Create a Neon database** at <https://neon.tech> (free tier is enough to start). Copy the connection string — Neon shows it in `psql` form like
   `postgresql://user:password@ep-xxx.us-east-2.aws.neon.tech/mantenimiento?sslmode=require`.
2. **Convert it to JDBC**: prepend `jdbc:`, drop `user:password@`, keep the SSL params. The result looks like
   `jdbc:postgresql://ep-xxx.us-east-2.aws.neon.tech/mantenimiento?sslmode=require&channel_binding=require`.
3. **Push the repo to GitHub** (private is fine).
4. **In Render**: `New +` → `Blueprint` → select the repo. Render parses `render.yaml` and asks for the env vars marked `sync: false`:

   | Variable | Value |
   |---|---|
   | `DB_URL` | The JDBC URL from step 2 |
   | `DB_USERNAME` | Neon user (from the connection string) |
   | `DB_PASSWORD` | Neon password |
   | `ADMIN_EMAIL` | Email for the bootstrap admin |
   | `ADMIN_PASSWORD` | Strong password — change after first login |
   | `CORS_ORIGINS` | Your frontend URL(s), comma-separated |

   `JWT_SECRET` is generated automatically by Render. `PORT` is injected by Render at runtime — the app reads it via `server.port=${PORT:8081}`.

5. **Deploy.** Render builds from `docker/Dockerfile`, runs Flyway on startup, and Spring boots against the Neon Postgres.

After the first successful boot, change the admin password via `POST /api/v1/auth/change-password`.

### Environment variables — full list

See [`.env.example`](.env.example). Quick reference:

| Variable | Purpose | Default |
|---|---|---|
| `PORT` | HTTP port (Render injects this) | `8081` |
| `SPRING_PROFILES_ACTIVE` | `dev` / `docker` / `prod` | `dev` locally, `prod` on Render |
| `DB_URL` | JDBC URL | local docker Postgres |
| `DB_USERNAME` / `DB_PASSWORD` | DB credentials | `postgres` / `admin` |
| `DB_POOL_SIZE` / `DB_POOL_MIN_IDLE` | Hikari pool sizing | `5` / `1` |
| `JWT_SECRET` | HS256 signing key (≥256 bits) | dev default |
| `JWT_EXPIRATION_MS` | Access token TTL | `3600000` (1h) |
| `JWT_REFRESH_EXPIRATION_MS` | Refresh token TTL | `86400000` (24h) |
| `ADMIN_USERNAME` / `ADMIN_EMAIL` / `ADMIN_PASSWORD` | Bootstrap admin | `admin` / `admin@example.com` / generated |
| `CORS_ORIGINS` | Comma-separated allowed origins | `http://localhost:3000` |

## Adding a new aggregate

For each new domain `Xyz` (e.g. `Product`, `Order`), create:

```
domain/
  model/xyz/Xyz.java                                # @Data @Builder
  model/xyz/gateway/XyzOutputPort.java
  usecase/input/XyzInputPort.java
  usecase/XyzUseCase.java                           # implements XyzInputPort
  constant/XyzMessages.java

application/
  handler/XyzHandler.java
  handler/adapter/XyzHandlerAdapter.java            # @Service @Transactional

infrastructure/
  drivenadapter/postgresql/entity/XyzEntity.java
  drivenadapter/postgresql/repository/XyzRepository.java
  drivenadapter/postgresql/mapper/XyzEntityMapper.java
  drivenadapter/postgresql/adapter/XyzPostgresqlAdapter.java
  entrypoint/dto/request/XyzRequest.java
  entrypoint/dto/response/XyzResponse.java
  entrypoint/mapper/XyzRestMapper.java
  entrypoint/rest/XyzRestController.java

config/BeanConfiguration.java                       # add @Bean for XyzInputPort
resources/db/migration/V2__add_xyz.sql              # next Flyway migration
```
