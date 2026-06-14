# Enterprise Learning Platform - Setup Guide

## 🚀 Getting Started

This guide will help you set up and run the Enterprise Learning Platform Phase 1 implementation.

## Option 1: Quick Start with Docker (Recommended)

### Prerequisites
- Docker (https://www.docker.com/products/docker-desktop)
- Docker Compose (usually included with Docker Desktop)

### Steps

1. **Navigate to project directory**
   ```bash
   cd learning-platform-implementation
   ```

2. **Start all services**
   ```bash
   docker-compose up -d
   ```

3. **Wait for services to start** (1-2 minutes)
   ```bash
   docker-compose logs -f
   ```

4. **Access the application**
   - Frontend: http://localhost
   - Backend API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - MySQL: localhost:3306

5. **Test the application**
   - Go to http://localhost/register
   - Create a new account
   - Login with your credentials

6. **Stop services**
   ```bash
   docker-compose down
   ```

---

## Option 2: Manual Setup

### Prerequisites

#### Java Backend
- Java 21+ (https://www.oracle.com/java/technologies/downloads/#java21)
- Maven 3.9+ (https://maven.apache.org/download.cgi)

#### Angular Frontend
- Node.js 20+ (https://nodejs.org/)
- npm 10+ (comes with Node.js)

#### Database
- MySQL 8.0+ (https://www.mysql.com/downloads/)

### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure database** (in `src/main/resources/application.yml`)
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/learning_platform
       username: learning_user
       password: learning_password
   ```

4. **Create database**
   ```bash
   mysql -u root -p < ../docs/database_init.sql
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Backend will be available at**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure API URL** (in `src/environments/environment.ts`)
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080'
   };
   ```

4. **Start development server**
   ```bash
   npm start
   ```

5. **Frontend will be available at**
   - http://localhost:4200

### Database Setup

1. **Create database and user**
   ```bash
   mysql -u root -p
   mysql> CREATE DATABASE learning_platform;
   mysql> CREATE USER 'learning_user'@'localhost' IDENTIFIED BY 'learning_password';
   mysql> GRANT ALL PRIVILEGES ON learning_platform.* TO 'learning_user'@'localhost';
   mysql> FLUSH PRIVILEGES;
   mysql> EXIT;
   ```

2. **Initialize database schema**
   ```bash
   mysql -u learning_user -p learning_platform < docs/database_init.sql
   ```

---

## 📝 Testing the Application

### Via Frontend UI

1. **Register a new user**
   - Go to http://localhost/register (or http://localhost:4200/register for manual setup)
   - Fill in the form with:
     - First Name: John
     - Last Name: Doe
     - Email: john@example.com
     - Password: password123
   - Click Register
   - You'll be logged in automatically

2. **Login**
   - Go to http://localhost/login
   - Enter your credentials
   - Click Login

### Via API (Swagger UI)

1. **Open Swagger UI**
   - http://localhost:8080/swagger-ui.html

2. **Register endpoint**
   - Click on "Authentication" section
   - Click on POST /api/v1/auth/register
   - Click "Try it out"
   - Enter request body:
     ```json
     {
       "email": "test@example.com",
       "firstName": "Test",
       "lastName": "User",
       "password": "password123",
       "confirmPassword": "password123"
     }
     ```
   - Click "Execute"

3. **Login endpoint**
   - Click on POST /api/v1/auth/login
   - Click "Try it out"
   - Enter request body:
     ```json
     {
       "email": "test@example.com",
       "password": "password123"
     }
     ```
   - Click "Execute"

### Via cURL

```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "curl@example.com",
    "firstName": "Curl",
    "lastName": "User",
    "password": "password123",
    "confirmPassword": "password123"
  }'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "curl@example.com",
    "password": "password123"
  }'
```

---

## 🔧 Common Issues & Solutions

### Issue: Port 3306 already in use
**Solution:** Stop MySQL or change port in application.yml
```bash
# Stop MySQL
sudo systemctl stop mysql

# Or change port in application.yml
spring.datasource.url: jdbc:mysql://localhost:3307/learning_platform
```

### Issue: Port 8080 already in use
**Solution:** Change port in application.yml
```yaml
server:
  port: 8081
```

### Issue: Port 4200 already in use (Angular)
**Solution:** Use different port
```bash
ng serve --port 4201
```

### Issue: MySQL connection refused
**Solution:** Check MySQL is running and credentials are correct
```bash
# Check if MySQL is running
mysql -u root -p

# Verify credentials in application.yml
spring.datasource.username: learning_user
spring.datasource.password: learning_password
```

### Issue: CORS errors in browser
**Solution:** Ensure backend is running and CORS is configured
- Backend must be running at http://localhost:8080
- Check SecurityConfig.java for CORS configuration

### Issue: Docker containers not starting
**Solution:** Check logs and rebuild
```bash
# View logs
docker-compose logs

# Rebuild images
docker-compose build --no-cache

# Start again
docker-compose up -d
```

---

## 📚 Project Structure

```
learning-platform-implementation/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/...      # Java source code
│   ├── src/main/resources/    # Configuration files
│   ├── pom.xml               # Maven configuration
│   └── Dockerfile            # Docker image definition
│
├── frontend/                   # Angular Frontend
│   ├── src/app/              # Angular components
│   ├── src/environments/     # Environment configs
│   ├── package.json          # npm configuration
│   ├── Dockerfile            # Docker image definition
│   └── nginx.conf            # Nginx configuration
│
├── docs/                       # Documentation
│   └── database_init.sql     # Database schema
│
├── docker-compose.yml        # Docker Compose configuration
└── README.md                 # Project overview
```

---

## 🔐 Security Notes

1. **Change JWT Secret** in production
   - Edit `backend/src/main/resources/application.yml`
   - Change `jwt.secret` to a strong, unique value

2. **Change Database Password** in production
   - Edit `docker-compose.yml` or `application.yml`
   - Use strong passwords

3. **Enable HTTPS** in production
   - Configure SSL/TLS certificates
   - Update CORS configuration for HTTPS URLs

4. **Update API URLs** in production
   - Edit `frontend/src/environments/environment.prod.ts`
   - Change `apiUrl` to production backend URL

---

## 📖 Documentation

- **Architecture:** See `../docs/01_System_Architecture.md`
- **Database Schema:** See `../docs/02_Database_Schema.md`
- **Backend Design:** See `../docs/03_Backend_Architecture.md`
- **Frontend Design:** See `../docs/04_Frontend_Architecture.md`
- **Deployment:** See `../docs/05_Deployment_Security.md`

---

## 🎯 Next Steps

1. **Explore the codebase**
   - Backend: `backend/src/main/java/com/enterprise/learning/`
   - Frontend: `frontend/src/app/`

2. **Customize the application**
   - Modify login/register pages
   - Add new features
   - Customize styling

3. **Deploy to production**
   - Follow deployment guide in docs
   - Configure GCP services
   - Set up CI/CD pipeline

---

## 💡 Tips

- Use Swagger UI for API testing: http://localhost:8080/swagger-ui.html
- Check backend logs: `docker-compose logs backend`
- Check frontend logs: `docker-compose logs frontend`
- Database is automatically initialized on first run
- JWT tokens expire after 15 minutes
- Refresh tokens last for 7 days

---

**Version:** 1.0.0  
**Last Updated:** June 14, 2026
