# Spring Boot Microservices Project

## Overview
Enterprise-grade microservices architecture built with Spring Boot 3.3.5, Spring Cloud 2023.0.3, Java 21, and secured with Keycloak OAuth2 Resource Server. This project demonstrates modern cloud-native development patterns including service discovery, API gateway, circuit breakers, and OAuth2 security.

## Current State
- **Date**: November 18, 2025
- **Status**: Fully migrated to Spring Boot 3 and Java 21 with Keycloak OAuth2 security
- **Migration**: Successfully upgraded from Spring Boot 2.5.5 (Java 11) to Spring Boot 3.3.5 (Java 21)

## Recent Changes

### November 18, 2025 - Spring Boot 3 Migration & Keycloak Security
- ✅ Upgraded all services to Spring Boot 3.3.5 and Spring Cloud 2023.0.3
- ✅ Migrated from Java 11 → Java 17 → Java 21
- ✅ Updated javax.persistence to jakarta.persistence for JPA compatibility
- ✅ Removed deprecated `@EnableEurekaClient` annotation (auto-configured in Spring Cloud 2023)
- ✅ Added OAuth2 Resource Server security to all business services
- ✅ Integrated Keycloak for centralized authentication and authorization
- ✅ Created security configuration classes with JWT validation
- ✅ Updated Resilience4J from spring-boot2 to spring-boot3 version
- ✅ Configured workflows for service execution
- ✅ Created comprehensive documentation and startup scripts

## Project Architecture

### Microservices Components

1. **Service Registry** (port 8761)
   - Eureka Server for service discovery
   - Central registry for all microservices
   - Dashboard for monitoring service health

2. **API Gateway** (port 9191)
   - Spring Cloud Gateway
   - OAuth2 Resource Server with JWT validation
   - Routes requests to backend services
   - Load balancing with Eureka integration

3. **User Service** (port 9003)
   - Business microservice for user management
   - OAuth2 protected endpoints
   - H2 in-memory database
   - Resilience4J circuit breaker
   - REST API for user operations

4. **Department Service** (port 9002)
   - Business microservice for department management
   - OAuth2 protected endpoints
   - H2 in-memory database
   - REST API for department operations

### Security Architecture

- **OAuth2 Resource Server Pattern**: All services validate JWT tokens
- **Keycloak Integration**: Central identity and access management
- **Stateless Authentication**: No server-side sessions
- **Method-level Security**: Fine-grained authorization with `@EnableMethodSecurity`

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.3.5 |
| Cloud Framework | Spring Cloud | 2023.0.3 |
| Build Tool | Maven | 3.9.9 |
| Security | Spring Security OAuth2 | 6.3.4 |
| Service Discovery | Netflix Eureka | 4.1.3 |
| API Gateway | Spring Cloud Gateway | 4.1.5 |
| Database | H2 | 2.2.224 (in-memory) |
| Resilience | Resilience4J | 2.1.0 |
| Identity Provider | Keycloak | Latest |

## User Preferences

### Development Workflow
- Build with Maven before running
- Services started in order: Registry → Business Services → Gateway
- Keycloak must be running before starting services

### Coding Conventions
- Use lombok for boilerplate reduction
- Jakarta EE packages (not javax)
- RESTful API design
- DTOs for data transfer
- Stateless service design

## Startup Instructions

### Quick Start

1. **Start Keycloak**:
   ```bash
   docker run -d --name keycloak -p 8080:8080 \
     -e KEYCLOAK_ADMIN=admin \
     -e KEYCLOAK_ADMIN_PASSWORD=admin \
     quay.io/keycloak/keycloak:latest start-dev
   ```

2. **Configure Keycloak**:
   - Access: http://localhost:8080
   - Create realm: `microservices`
   - Create client: `microservices-client`

3. **Start All Services**:
   ```bash
   ./start-all-services.sh
   ```

### Manual Startup (Replit Workflow)

The **Service Registry** workflow is pre-configured in Replit. To run:
1. Click the "Run" button - starts Service Registry automatically
2. Services register themselves with Eureka at http://localhost:8761

For other services, open terminals and run:
```bash
# Terminal 2 - Department Service
cd department-service && mvn spring-boot:run

# Terminal 3 - User Service  
cd user-service && mvn spring-boot:run

# Terminal 4 - API Gateway
cd cloud-api-gateway && mvn spring-boot:run
```

## API Endpoints

### Through API Gateway (OAuth2 Protected)

```bash
# Get access token from Keycloak
TOKEN=$(curl -X POST 'http://localhost:8080/realms/microservices/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'client_id=microservices-client' \
  -d 'client_secret=YOUR_SECRET' \
  -d 'grant_type=client_credentials' | jq -r '.access_token')

# Call protected endpoints
curl -H "Authorization: Bearer $TOKEN" http://localhost:9191/api/users
curl -H "Authorization: Bearer $TOKEN" http://localhost:9191/api/departments
```

### Direct Access (Development)

- Eureka Dashboard: http://localhost:8761
- H2 Console (User): http://localhost:9003/h2-console
- H2 Console (Dept): http://localhost:9002/h2-console
- Actuator Health: http://localhost:{port}/actuator/health

## Environment Variables

Required for OAuth2:
```bash
export KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/microservices
export KEYCLOAK_JWK_SET_URI=http://localhost:8080/realms/microservices/protocol/openid-connect/certs
```

## Files & Structure

```
.
├── service-registry/          # Eureka Server (Port 8761)
│   ├── src/main/java/         # Application code
│   ├── src/main/resources/config/application.yml
│   └── pom.xml                # Maven dependencies
│
├── cloud-api-gateway/         # API Gateway (Port 9191)
│   ├── src/main/java/
│   │   └── config/SecurityConfig.java  # OAuth2 security
│   ├── src/main/resources/config/application.yml
│   └── pom.xml
│
├── user-service/              # User Service (Port 9003)
│   ├── src/main/java/
│   │   ├── config/SecurityConfig.java
│   │   ├── controller/        # REST controllers
│   │   ├── model/             # JPA entities
│   │   ├── repository/        # Data access
│   │   └── services/          # Business logic
│   ├── src/main/resources/config/application.yml
│   └── pom.xml
│
├── department-service/        # Department Service (Port 9002)
│   ├── src/main/java/
│   │   ├── config/SecurityConfig.java
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── services/
│   ├── src/main/resources/config/application.yml
│   └── pom.xml
│
├── README.md                  # Comprehensive documentation
├── start-all-services.sh      # Startup script
└── replit.md                  # This file - project memory
```

## Key Configuration Changes

### Spring Boot 3 Migration Changes

1. **Package Migration**: `javax.*` → `jakarta.*`
   - javax.persistence → jakarta.persistence
   - Affects all JPA entities

2. **Deprecated Annotations Removed**:
   - `@EnableEurekaClient` no longer needed
   - Auto-configuration handles Eureka client setup

3. **Dependency Updates**:
   - spring-boot-starter-parent: 2.5.5 → 3.3.5
   - spring-cloud-dependencies: 2020.0.4 → 2023.0.3
   - resilience4j-spring-boot2 → resilience4j-spring-boot3

4. **Java Version**: 11 → 21
   - Takes advantage of Java 17+ features
   - Record types, pattern matching, etc.

### Security Configuration

All services (except Service Registry) include:

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // OAuth2 Resource Server with JWT
        // Stateless sessions
        // CSRF disabled for REST APIs
    }
}
```

## Troubleshooting

### Common Issues

1. **Services not registering with Eureka**
   - Ensure Service Registry starts first
   - Wait 30 seconds for full startup
   - Check logs for connection errors

2. **OAuth2 authentication failures**
   - Verify Keycloak is running on port 8080
   - Ensure realm 'microservices' exists
   - Check client credentials are correct
   - Verify JWT token is not expired

3. **Port conflicts**
   - Default ports: 8761, 9191, 9003, 9002, 8080
   - Change in application.yml if needed

4. **Build failures**
   - Ensure Java 21 is installed
   - Clear Maven cache: `mvn clean`
   - Check internet connectivity for dependencies

## Next Steps & Future Enhancements

### Recommended Additions

1. **Database**: Replace H2 with PostgreSQL for production
2. **Configuration Server**: Centralize config with Spring Cloud Config
3. **Distributed Tracing**: Add Sleuth + Zipkin for request tracing
4. **API Documentation**: Integrate SpringDoc OpenAPI
5. **Monitoring**: Add Prometheus + Grafana
6. **Messaging**: Implement event-driven communication with RabbitMQ/Kafka
7. **Docker**: Containerize all services
8. **Kubernetes**: Deploy to K8s with Helm charts

### Production Readiness Checklist

- [ ] Replace H2 with production database
- [ ] Configure SSL/TLS for all services
- [ ] Implement proper logging (ELK stack)
- [ ] Set up CI/CD pipeline
- [ ] Configure external configuration management
- [ ] Implement rate limiting
- [ ] Add comprehensive integration tests
- [ ] Set up monitoring and alerting
- [ ] Configure backup and disaster recovery
- [ ] Implement API versioning
- [ ] Add API documentation with Swagger

## References

- [README.md](README.md) - Complete documentation with examples
- [Spring Boot 3 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)

## Maintenance Notes

- All services built and tested successfully with Java 21
- Maven builds complete without errors
- Workflow configured for Service Registry in Replit
- All dependencies resolved and compatible with Spring Boot 3
- No deprecated APIs in use (as of November 2025)
