# Spring Boot Microservices with Keycloak Security

A complete microservices architecture built with **Spring Boot 3.3.5**, **Spring Cloud 2023.0.3**, **Java 21**, and secured with **Keycloak OAuth2 Resource Server**.

## Architecture Overview

This project implements a microservices architecture with the following components:

- **Service Registry** (Port 8761) - Eureka Server for service discovery
- **API Gateway** (Port 9191) - Spring Cloud Gateway with OAuth2 security
- **User Service** (Port 9003) - Business microservice managing user data
- **Department Service** (Port 9002) - Business microservice managing department data

All services are secured using OAuth2 Resource Server pattern with Keycloak as the identity provider.

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.3.5
- **Spring Cloud**: 2023.0.3
- **Build Tool**: Maven 3.9.9
- **Security**: Spring Security OAuth2 Resource Server
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Database**: H2 (In-Memory)
- **Resilience**: Resilience4J (Circuit Breaker pattern)

## Key Features

### Migration to Spring Boot 3
- Upgraded from Spring Boot 2.5.5 to 3.3.5
- Migrated from javax.* to jakarta.* packages
- Updated to Java 21 (minimum Java 17 required)
- Updated Spring Cloud dependencies to 2023.0.3
- Removed deprecated `@EnableEurekaClient` annotation (auto-configured in Spring Cloud 2023)

### OAuth2 Security with Keycloak
- All business services protected as OAuth2 Resource Servers
- JWT token validation on API Gateway
- Stateless session management
- Keycloak integration for authentication and authorization

## Project Structure

```
microservices-don/
├── service-registry/          # Eureka Server
├── cloud-api-gateway/         # API Gateway with OAuth2
├── user-service/              # User Management Service
├── department-service/        # Department Management Service
└── README.md
```

## Prerequisites

1. **Java 21** - Installed and configured
2. **Maven 3.9+** - For building the projects
3. **Keycloak** - Identity and Access Management server

## Keycloak Setup

Before running the microservices, you need to set up Keycloak:

### Option 1: Docker (Recommended)

```bash
docker run -d \
  --name keycloak \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

### Option 2: Download and Run

1. Download Keycloak from https://www.keycloak.org/downloads
2. Extract and run:
   ```bash
   bin/kc.sh start-dev
   ```

### Configure Keycloak Realm

1. Access Keycloak Admin Console: `http://localhost:8080`
2. Login with admin credentials
3. Create a new realm named `microservices`
4. Create a client for your application:
   - Client ID: `microservices-client`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Valid Redirect URIs: `*`
5. Create roles and users as needed

### Environment Variables

Set these environment variables or update application.yml files:

```bash
export KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/microservices
export KEYCLOAK_JWK_SET_URI=http://localhost:8080/realms/microservices/protocol/openid-connect/certs
```

## How to Build

Build all services:

```bash
# Build Service Registry
cd service-registry
mvn clean package -DskipTests

# Build API Gateway
cd ../cloud-api-gateway
mvn clean package -DskipTests

# Build User Service
cd ../user-service
mvn clean package -DskipTests

# Build Department Service
cd ../department-service
mvn clean package -DskipTests
```

Or build all at once from root:

```bash
for service in service-registry cloud-api-gateway user-service department-service; do
  (cd $service && mvn clean package -DskipTests)
done
```

## How to Run

**Important**: Services must be started in the following order to ensure proper registration with Eureka:

### Step 1: Start Keycloak (if not already running)

```bash
# Using Docker
docker start keycloak

# Or standalone
cd keycloak-{version}
bin/kc.sh start-dev
```

### Step 2: Start Service Registry (Eureka Server)

```bash
cd service-registry
mvn spring-boot:run
```

Wait for the service to start completely. You can access Eureka Dashboard at:
- URL: http://localhost:8761

### Step 3: Start Business Services

In separate terminals, start the following services:

**User Service:**
```bash
cd user-service
mvn spring-boot:run
```

**Department Service:**
```bash
cd department-service
mvn spring-boot:run
```

### Step 4: Start API Gateway

```bash
cd cloud-api-gateway
mvn spring-boot:run
```

## Service Endpoints

### Eureka Dashboard
- URL: http://localhost:8761
- View all registered services

### API Gateway
- URL: http://localhost:9191
- Routes:
  - `/api/users/**` → User Service
  - `/api/departments/**` → Department Service

### Direct Service Access (Development Only)
- User Service: http://localhost:9003
- Department Service: http://localhost:9002

## Testing with OAuth2

### 1. Obtain Access Token from Keycloak

```bash
curl -X POST 'http://localhost:8080/realms/microservices/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'client_id=microservices-client' \
  -d 'client_secret=YOUR_CLIENT_SECRET' \
  -d 'grant_type=client_credentials'
```

### 2. Call Protected Endpoints

```bash
# Example: Get users through API Gateway
curl -X GET 'http://localhost:9191/api/users' \
  -H 'Authorization: Bearer YOUR_ACCESS_TOKEN'

# Example: Get departments through API Gateway
curl -X GET 'http://localhost:9191/api/departments' \
  -H 'Authorization: Bearer YOUR_ACCESS_TOKEN'
```

## Security Configuration

All microservices (except Service Registry) are configured as OAuth2 Resource Servers:

- **Stateless Sessions**: No session state maintained on servers
- **JWT Validation**: Tokens validated against Keycloak
- **Method Security**: Fine-grained authorization with `@EnableMethodSecurity`
- **CSRF Disabled**: Appropriate for stateless REST APIs

### Unsecured Endpoints

The following endpoints are accessible without authentication:
- Actuator endpoints: `/actuator/**`
- H2 Console (dev only): `/h2-console/**`
- Eureka endpoints: `/eureka/**`

## Development Features

### H2 Database Console
Access H2 console for debugging (development only):
- URL: http://localhost:9003/h2-console (User Service)
- URL: http://localhost:9002/h2-console (Department Service)
- JDBC URL: `jdbc:h2:mem:testdb`

### Resilience4J Circuit Breaker
User Service includes circuit breaker configuration for resilient inter-service communication.

### Actuator Endpoints
Health and metrics available at:
- http://localhost:9003/actuator/health
- http://localhost:9002/actuator/health
- http://localhost:9191/actuator/health

## Configuration Files

All services use externalized configuration in `src/main/resources/config/application.yml`

### Key Configuration Properties

**Service Registry:**
```yaml
server.port: 8761
eureka.client.register-with-eureka: false
eureka.client.fetch-registry: false
```

**Business Services:**
```yaml
server.port: 9003 # or 9002
spring.security.oauth2.resourceserver.jwt:
  issuer-uri: ${KEYCLOAK_ISSUER_URI}
  jwk-set-uri: ${KEYCLOAK_JWK_SET_URI}
eureka.client.service-url.defaultZone: http://localhost:8761/eureka/
```

**API Gateway:**
```yaml
server.port: 9191
spring.cloud.gateway.routes:
  - id: USER-SERVICE
    uri: lb://USER-SERVICE
    predicates: [Path=/api/users/**]
```

## Troubleshooting

### Services Not Registering with Eureka
- Ensure Service Registry is started first
- Check network connectivity on port 8761
- Verify `eureka.client.service-url.defaultZone` is correct

### Authentication Failures
- Verify Keycloak is running on port 8080
- Check realm name is `microservices`
- Ensure `KEYCLOAK_ISSUER_URI` and `KEYCLOAK_JWK_SET_URI` are set correctly
- Verify JWT token is not expired

### Port Conflicts
- Check if ports 8761, 9191, 9002, 9003, 8080 are available
- Modify ports in application.yml if needed

## Production Deployment

For production deployment:

1. **Replace H2 with Production Database** (PostgreSQL, MySQL, etc.)
2. **Configure Keycloak** with proper SSL/TLS
3. **Update application.yml** with production values
4. **Enable SSL/TLS** on all services
5. **Configure proper logging** and monitoring
6. **Set up external configuration server** (Spring Cloud Config)
7. **Implement distributed tracing** (Sleuth + Zipkin)

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/3.3.5/reference/html/)
- [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Netflix Eureka](https://github.com/Netflix/eureka/wiki)

## License

This project is for educational purposes.

## Author

Don Eugene (doneugene2008)
