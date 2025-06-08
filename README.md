# User Management API

A Spring Boot REST API for managing users, based on the JSONPlaceholder user model. This project includes JWT authentication, PostgreSQL database, and Docker support.

## Features

- Full CRUD operations for users
- JWT-based authentication
- PostgreSQL database with Liquibase migrations
- Docker and Docker Compose support
- Structured data model with nested objects (address, geo, company)

## Prerequisites

- Java 21
- Docker and Docker Compose
- Maven (optional, wrapper included)

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Start the application using Docker Compose:

```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080/api`

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Users

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Authentication

All user endpoints require JWT authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer your_jwt_token_here
```

## Development

To run the application locally without Docker:

1. Start a PostgreSQL instance
2. Update `application.yml` with your database configuration
3. Run the application:

```bash
./mvnw spring-boot:run
```

## Database Migrations

Database migrations are managed by Liquibase. Migration files are located in:
`src/main/resources/db/changelog/`

## Sample Data

The application includes sample data from JSONPlaceholder, loaded via Liquibase migrations.
