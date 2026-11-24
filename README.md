# JWT Authentication API — Spring Boot

A Java 17 / Spring Boot REST API demonstrating signup, login, role-based authorization, and stateless JWT authentication.

## Technology stack

- Java 17
- Spring Boot 3.0.0
- Spring Web, Spring Data JPA, Spring Security
- MySQL
- Auth0 Java JWT 3.3.0
- Maven Wrapper

## Project structure

```text
src/main/java/com/hms/
  confi/       # JWT filter and security configuration
  controller/  # User and country REST controllers
  entity/      # JPA user entity
  payload/     # Login and token DTOs
  repository/  # Spring Data repositories
  service/     # Login and JWT services
src/main/resources/application.properties
src/test/java/
```

## Prerequisites

- JDK 17
- MySQL 8+
- Maven is optional; the Maven Wrapper is included

## Environment configuration

The application reads secrets from environment variables. Do not commit real database passwords or JWT signing keys.

| Variable | Purpose | Local example |
|---|---|---|
| DB_URL | JDBC connection | jdbc:mysql://localhost:3306/hmsdb |
| DB_USERNAME | Database user | root |
| DB_PASSWORD | Database password | your-local-password |
| JWT_SECRET | HMAC signing key, at least 32 characters | generate a unique random secret |
| JWT_ISSUER | Token issuer | hms-api |
| JWT_EXPIRY_MS | Token lifetime in milliseconds | 86400000 |

Create the database before starting the app:

```sql
CREATE DATABASE hmsdb;
```

Set the environment variables in your terminal or IDE Run Configuration. An example placeholder file is available in `.env.example`; Spring Boot does not automatically load that file, so export the values or configure them in your IDE.

## Run locally

Windows:

```bat
set DB_PASSWORD=your-local-password
set JWT_SECRET=replace-with-a-random-secret-of-at-least-32-characters
mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
export DB_PASSWORD='your-local-password'
export JWT_SECRET='replace-with-a-random-secret-of-at-least-32-characters'
./mvnw spring-boot:run
```

Build and run tests:

```bash
./mvnw clean verify
```

On Windows use `mvnw.cmd clean verify`.

## API endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/api/v1/users/signup` | Public | Register a standard user |
| POST | `/api/v1/users/signup-property-owner` | Public | Register a property owner |
| POST | `/api/v1/users/login` | Public | Verify credentials and receive a JWT |
| GET | `/api/v1/users/message` | JWT required | Example protected endpoint |
| POST | `/api/v1/country/addCountry` | OWNER or ADMIN | Example role-protected endpoint |

### Signup request

```json
{
  "username": "neeraj",
  "email": "neeraj@example.com",
  "name": "Neeraj Kumar",
  "password": "Use-A-Strong-Password"
}
```

### Login request

```json
{
  "username": "neeraj",
  "password": "Use-A-Strong-Password"
}
```

The login response contains a token. Send it on protected requests:

```http
Authorization: Bearer <token>
```

## Security notes

- Password hashes are excluded from JSON responses.
- New passwords are hashed with BCrypt strength 10.
- JWTs are verified for signature and issuer and include an expiry.
- The API uses stateless Spring Security sessions.
- The signing key and database password must be supplied outside source control.
- This demo's public property-owner signup endpoint grants the OWNER role to anyone who calls it. **Do not expose that endpoint in a real application**; owner/admin roles must be granted only by a trusted administrator or verified workflow.
- Existing users created with older BCrypt hashes can still log in; BCrypt stores its work factor in each hash.

## Tests

The JWT service unit tests cover token generation, successful verification, signature tampering, and blank usernames. Run `./mvnw clean verify`.

## Scope

This is a learning project, not a production-ready identity provider. Before production use, add DTO validation, rate limiting, refresh-token/revocation strategy, centralized error handling, integration tests, and controlled role provisioning. Consider upgrading the older Spring Boot and JWT library versions after compatibility testing.
