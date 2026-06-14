# Enterprise Training & AI Learning Platform - Phase 1 Implementation

## 🚀 Quick Start

This is the **Phase 1 Implementation** of the Enterprise Training & AI Learning Platform with complete User Management & Authentication system.

### Prerequisites

- Docker & Docker Compose (recommended for quick setup)
- OR Java 21+, Node.js 20+, MySQL 8.0+, Maven 3.9+

### Option 1: Run with Docker Compose (Recommended)

```bash
# Start all services
docker-compose up -d

# Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# MySQL: localhost:3306

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Option 2: Manual Setup

#### Backend Setup

```bash
cd backend

# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run

# Backend will be available at http://localhost:8080
# Swagger UI at http://localhost:8080/swagger-ui.html
```

#### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start

# Frontend will be available at http://localhost:4200
```

#### Database Setup

```bash
# Create database
mysql -u root -p < ../docs/database_init.sql

# Or manually:
mysql -u root -p
mysql> CREATE DATABASE learning_platform;
mysql> USE learning_platform;
mysql> SOURCE /path/to/database_init.sql;
```

## 📁 Project Structure

```
learning-platform-implementation/
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/enterprise/learning/
│   │   ├── entity/                  # JPA Entities (User, Role)
│   │   ├── repository/              # Spring Data Repositories
│   │   ├── service/                 # Business Logic (AuthService)
│   │   ├── controller/              # REST Controllers (AuthController)
│   │   ├── dto/                     # Data Transfer Objects
│   │   ├── config/                  # Configuration (Security, OpenAPI)
│   │   ├── security/                # Security Components (JWT, Filters)
│   │   └── LearningPlatformApplication.java
│   ├── src/main/resources/
│   │   └── application.yml          # Spring Boot Configuration
│   ├── pom.xml                      # Maven Configuration
│   └── Dockerfile
│
├── frontend/                         # Angular Frontend
│   ├── src/app/
│   │   ├── core/
│   │   │   ├── services/           # AuthService
│   │   │   ├── guards/             # AuthGuard
│   │   │   └── interceptors/       # JwtInterceptor
│   │   ├── features/
│   │   │   └── auth/
│   │   │       ├── login/          # Login Component
│   │   │       └── register/       # Register Component
│   │   └── app.component.ts
│   ├── src/environments/           # Environment configs
│   ├── package.json
│   ├── Dockerfile
│   └── nginx.conf
│
├── docker-compose.yml              # Docker Compose Configuration
├── .gitignore
└── README.md
```

## 🔐 Authentication Flow

### Registration
1. User fills registration form (First Name, Last Name, Email, Password)
2. Frontend validates form
3. POST to `/api/v1/auth/register`
4. Backend creates user with LEARNER role
5. JWT token returned
6. User logged in automatically

### Login
1. User enters email and password
2. Frontend validates form
3. POST to `/api/v1/auth/login`
4. Backend validates credentials
5. JWT token returned
6. Token stored in localStorage
7. Token added to all API requests via JWT Interceptor

### Token Refresh
1. When JWT expires (15 minutes)
2. JWT Interceptor detects 401 response
3. Refresh token used to get new JWT
4. Request retried with new token

## 🔌 API Endpoints

### Authentication Endpoints

| Endpoint | Method | Body | Response |
| :--- | :--- | :--- | :--- |
| `/api/v1/auth/register` | POST | RegisterRequest | AuthResponse |
| `/api/v1/auth/login` | POST | AuthRequest | AuthResponse |
| `/api/v1/auth/refresh-token` | POST | refreshToken param | AuthResponse |

### Request/Response Examples

#### Register
```bash
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password123",
  "confirmPassword": "password123"
}

Response:
{
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "roles": ["LEARNER"],
  "message": "User registered successfully"
}
```

#### Login
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response:
{
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "roles": ["LEARNER"],
  "message": "Login successful"
}
```

## 🧪 Testing

### Test Credentials

After running `docker-compose up`, the database is initialized with roles. You can register a new user:

1. Go to http://localhost/register
2. Fill in the form
3. Click Register
4. You'll be logged in automatically

Or use the API directly:

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

### API Testing with Swagger

1. Open http://localhost:8080/swagger-ui.html
2. Click on "Authentication" section
3. Try out the endpoints

## 🔒 Security Features

- **JWT Authentication:** Stateless token-based authentication
- **Password Hashing:** BCrypt encryption
- **CORS:** Configured for frontend access
- **HTTPS Ready:** Can be deployed with SSL/TLS
- **Role-Based Access:** RBAC with 6 role types
- **Token Expiration:** 15-minute JWT expiration
- **Refresh Token:** 7-day refresh token for session extension

## 📝 Configuration

### Backend Configuration (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/learning_platform
    username: learning_user
    password: learning_password

jwt:
  secret: your-super-secret-jwt-key-change-in-production
  expiration: 900000  # 15 minutes
  refresh-expiration: 604800000  # 7 days
```

### Frontend Configuration (environment.ts)

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};
```

## 🛠️ Development

### Adding New Features

1. **Backend:** Add new entity → Repository → Service → Controller
2. **Frontend:** Add new service → Component → Route

### Running Tests

```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
```

## 📦 Deployment

### Docker Build

```bash
# Build backend image
docker build -t learning-platform-backend:latest ./backend

# Build frontend image
docker build -t learning-platform-frontend:latest ./frontend

# Run with docker-compose
docker-compose up -d
```

### GCP Deployment

```bash
# Push to Google Artifact Registry
docker tag learning-platform-backend:latest gcr.io/YOUR_PROJECT/backend:latest
docker push gcr.io/YOUR_PROJECT/backend:latest

# Deploy to Cloud Run
gcloud run deploy learning-platform-backend \
  --image gcr.io/YOUR_PROJECT/backend:latest \
  --platform managed \
  --region us-central1
```

## 🐛 Troubleshooting

| Issue | Solution |
| :--- | :--- |
| MySQL connection refused | Ensure MySQL is running and credentials match application.yml |
| Port already in use | Change port in application.yml or stop conflicting service |
| CORS errors | Check CORS configuration in SecurityConfig |
| JWT token invalid | Ensure JWT secret is the same in backend config |
| Frontend can't reach backend | Check API URL in environment.ts and backend is running |

## 📚 Documentation

- **Architecture:** See `../docs/01_System_Architecture.md`
- **Database Schema:** See `../docs/02_Database_Schema.md`
- **Backend Design:** See `../docs/03_Backend_Architecture.md`
- **Frontend Design:** See `../docs/04_Frontend_Architecture.md`
- **Deployment:** See `../docs/05_Deployment_Security.md`

## 🎯 Next Steps

1. **Phase 2:** Core LMS features (Courses, Modules, Lessons)
2. **Phase 3:** Assessment System & Coding Lab
3. **Phase 4:** Analytics & Reporting

## 📞 Support

For issues or questions, refer to the comprehensive documentation in the `docs/` directory.

---

**Status:** Phase 1 Complete - User Management & Authentication  
**Version:** 1.0.0  
**Last Updated:** June 14, 2026
