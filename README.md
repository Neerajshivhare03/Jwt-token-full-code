# JWT Authentication - Spring Boot

A Spring Boot application demonstrating user management and JSON Web Token (JWT)-based authentication.

## Technology stack

- Java 17
- Spring Boot 3.0.0
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- Auth0 Java JWT 3.3.0
- Maven

## Project structure

```text
src/
  main/
    java/com/hms/
      confi/
        JWTFilter.java
        SecurityConfig.java
      controller/
        CountryController.java
        UserController.java
      entity/
        AppUser.java
      payload/
        LoginDto.java
        TokenDto.java
      repository/
        AppUserRepository.java
      service/
        JWTService.java
        UserService.java
      HmsApplication.java
    resources/
      application.properties
  test/
pom.xml
mvnw
mvnw.cmd
```

## Prerequisites

- JDK 17
- MySQL
- Maven (optional; the Maven Wrapper is included)

## Configuration

1. Create a local MySQL database for this application.
2. Configure the datasource in `src/main/resources/application.properties`.
3. Configure a strong JWT signing secret using your local configuration.
4. Keep database passwords and JWT secrets out of source control. Use environment variables or a local, untracked configuration file.

Do not reuse the example project with production credentials or a hard-coded development secret.

## Run locally

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

Build and run tests:

```bash
./mvnw clean verify
```

## Main areas

- User and country REST controllers
- User persistence with Spring Data JPA
- Spring Security configuration
- JWT request filter and token service
- Login and token payload DTOs

Check the controller mappings in the source code for the exact API paths and request formats.

## Recommended next improvements

- Add API examples and a Postman collection.
- Add tests for login, token validation, and protected endpoints.
- Add request validation and centralized exception handling.
- Document the exact API contract and authentication flow.
- Review JWT expiry, signing algorithm, and secret management before deployment.

## Status

This repository is a learning/demo project. Review and test the security configuration before using it in any real application.
