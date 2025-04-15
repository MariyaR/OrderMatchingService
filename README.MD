# OrderMatchingService

## Run Locally

### 1. Build the project

```bash
./mvnw clean package -DskipTests
```

### 2. Start all services

```bash
docker-compose up --build
```

### 3. Test API

```bash
curl -X POST http://localhost:8080/kafka/send \
     -H "Content-Type: application/json" \
     -d '{"text": "hello kafka"}'
```
