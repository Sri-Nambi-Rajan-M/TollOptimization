# Urban Traffic Platform

A small CRUD application for managing commuters, toll transactions, vehicle types, and complaints.

## Requirements

- Java 21
- Maven 3.9+
- Node.js 20
- MySQL 8 when using the MySQL profile (H2 is the default for local development)

## Start the backend

```powershell
cd backend
mvn spring-boot:run
```

The default `standalone` profile uses an in-memory H2 database and applies the JPA entities with `spring.jpa.hibernate.ddl-auto=update`.

To use MySQL, configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`, then run:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

The MySQL profile uses `ddl-auto=update` and can create the configured database when the MySQL account has permission. The application does not create database users or grant permissions.

## Start the frontend

```powershell
cd frontend
npm install
npm run dev
```

The frontend uses `http://localhost:8080/api` by default. Set `VITE_API_BASE` to override it.

## CRUD API

Each resource supports `GET` and `POST` on its collection path and `PUT` and `DELETE` on `/{id}`:

- `/api/commuters`
- `/api/toll-transactions`
- `/api/vehicle-types`
- `/api/complaints`

The API is unauthenticated for this local CRUD application. All other `/api/**` routes are denied.
