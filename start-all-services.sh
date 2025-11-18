#!/bin/bash

echo "=================================="
echo "Starting Spring Boot Microservices"
echo "=================================="
echo ""

# Check if Keycloak is running
echo "Step 1: Checking Keycloak..."
if curl -s http://localhost:8080 > /dev/null 2>&1; then
    echo "✓ Keycloak is running on port 8080"
else
    echo "⚠ WARNING: Keycloak is not running on port 8080"
    echo "  Please start Keycloak before running the services:"
    echo "  docker run -d -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev"
    echo ""
fi

# Start Service Registry
echo "Step 2: Starting Service Registry (Eureka Server)..."
cd service-registry
mvn spring-boot:run > ../logs/service-registry.log 2>&1 &
REGISTRY_PID=$!
echo "  Service Registry starting with PID: $REGISTRY_PID"
cd ..

# Wait for Service Registry to be ready
echo "  Waiting for Service Registry to start..."
sleep 20

# Start Department Service
echo "Step 3: Starting Department Service..."
cd department-service
mvn spring-boot:run > ../logs/department-service.log 2>&1 &
DEPT_PID=$!
echo "  Department Service starting with PID: $DEPT_PID"
cd ..
sleep 10

# Start User Service
echo "Step 4: Starting User Service..."
cd user-service
mvn spring-boot:run > ../logs/user-service.log 2>&1 &
USER_PID=$!
echo "  User Service starting with PID: $USER_PID"
cd ..
sleep 10

# Start API Gateway
echo "Step 5: Starting API Gateway..."
cd cloud-api-gateway
mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
GATEWAY_PID=$!
echo "  API Gateway starting with PID: $GATEWAY_PID"
cd ..

echo ""
echo "=================================="
echo "All services started!"
echo "=================================="
echo ""
echo "Service URLs:"
echo "  - Eureka Dashboard:  http://localhost:8761"
echo "  - API Gateway:       http://localhost:9191"
echo "  - User Service:      http://localhost:9003"
echo "  - Department Service: http://localhost:9002"
echo "  - Keycloak:          http://localhost:8080"
echo ""
echo "Process IDs:"
echo "  - Service Registry:   $REGISTRY_PID"
echo "  - Department Service: $DEPT_PID"
echo "  - User Service:       $USER_PID"
echo "  - API Gateway:        $GATEWAY_PID"
echo ""
echo "Logs are available in the logs/ directory"
echo ""
echo "To stop all services, run:"
echo "  kill $REGISTRY_PID $DEPT_PID $USER_PID $GATEWAY_PID"
echo ""
