# Account Service

A microservice responsible for user account management, authentication, and profile handling in the CureMap platform. This service provides RESTful APIs for user registration, profile management, and authentication.

## 🚀 Features

- User registration and management
- Profile creation and updates
- Integration with Eureka Service Discovery
- RESTful API endpoints
- Database persistence with PostgreSQL
- Caching with Redis
- API documentation with Swagger/OpenAPI
- Health monitoring with Spring Boot Actuator

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **Database**: PostgreSQL
- **Caching**: Redis
- **Service Discovery**: Spring Cloud Netflix Eureka
- **API Documentation**: SpringDoc OpenAPI
- **Build Tool**: Maven
- **Containerization**: Docker

## 📦 Prerequisites

- Java 21 or later
- Maven 3.6.3 or later
- PostgreSQL 13+
- Redis 6.0+
- Eureka Server (for service discovery)

## 🚀 Getting Started

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd account-service
   ```

2. **Configure Database**
   - Ensure PostgreSQL is running on port 5433
   - Create a database named `postgres`
   - Update database credentials in `src/main/resources/application.yaml` if needed

3. **Run Redis**
   - Ensure Redis is running on localhost:6379
   - Or update Redis configuration in `application.yaml`

4. **Build the application**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**
   - API Docs: http://localhost:8081/swagger-ui.html
   - Actuator: http://localhost:8081/actuator

## 🏗️ Project Structure

```
src/main/java/com/custard/account_service/
├── adapter/                # Adapters layer (REST controllers, mappers)
│   ├── rest/               # REST API endpoints
│   └── mapper/             # Mappers between DTOs and domain models
├── application/            # Application layer (use cases, services)
│   ├── commands/           # Command objects for API requests
│   ├── exceptions/         # Custom exceptions
│   ├── mapper/             # Mappers between commands and domain models
│   └── usecases/           # Application use cases
├── domain/                 # Domain layer
│   ├── models/             # Domain models
│   └── ports/              # Ports (interfaces for secondary adapters)
└── infrastructure/         # Infrastructure layer
    ├── config/             # Spring configurations
    ├── database/           # Database entities and repositories
    └── mapper/             # Mappers between domain and persistence models
```

## 🌐 API Endpoints

### User Management
- `POST /api/users` - Register a new user
- `GET /api/users/{id}` - Get user by ID
- `PATCH /api/users/profile` - Update user profile
- `GET /api/users/profile/{userId}` - Get user profile

### Health & Monitoring
- `GET /actuator/health` - Application health check
- `GET /actuator/info` - Application info
- `GET /actuator/metrics` - Application metrics

## 🔧 Configuration

Configuration is managed through `application.yaml`:

```yaml
server:
  port: 8081

spring:
  application:
    name: ACCOUNTS
  datasource:
    url: jdbc:postgresql://localhost:5433/postgres
    username: admin
    password: admin
  jpa:
    hibernate:
      ddl-auto: update
  redis:
    host: localhost
    port: 6379

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8070/eureka/
```

## 🐳 Docker Support

To build and run the application using Docker:

```bash
# Build the application
mvn clean package

# Build Docker image
docker build -t account-service .

# Run the container
docker run -p 8081:8081 account-service
```

## 🧪 Testing

Run tests using Maven:

```bash
mvn test
```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📫 Contact

Project Link: [https://github.com/yourusername/account-service](https://github.com/yourusername/account-service)
