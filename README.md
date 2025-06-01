# Spring Boot Shopping Website 🛒

A comprehensive RESTful shopping website built with Spring Boot, featuring complete user management, advanced product search, role-based security, JWT authentication, and production-ready configurations.

## 🚀 Features

- **User Authentication & Authorization**
  - JWT-based authentication with refresh tokens
  - Role-based access control (ADMIN, USER)
  - Secure user registration and login
  - **All endpoints require authentication** (except auth and documentation)

- **User Management System**
  - Complete user CRUD operations
  - User profile management
  - Role assignment and management
  - Paginated user listing for admins
  - Self-service profile updates

- **Advanced Product Management**
  - CRUD operations for products
  - **Advanced search with multiple filters**
    - Keyword search across name and description
    - Category filtering
    - Price range filtering
    - Stock status filtering
    - Configurable pagination and sorting
  - Category-based product organization
  - Stock quantity tracking

- **Category Management**
  - CRUD operations for categories
  - Hierarchical category structure
  - Category-based product filtering

- **Performance & Caching**
  - Spring Cache integration
  - Cached categories, products, and user data
  - Optimized database queries

- **Production Ready**
  - Environment-based configuration
  - Health monitoring with Spring Boot Actuator
  - Security headers and HTTPS support
  - Connection pooling
  - Production and development profiles

- **Comprehensive API Documentation**
  - Interactive Swagger UI with authentication
  - Detailed endpoint descriptions
  - Request/response examples
  - Security scheme documentation

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3.5.0, Java 21
- **Database:** PostgreSQL with connection pooling
- **Security:** Spring Security, JWT with role-based access
- **Caching:** Spring Cache
- **Monitoring:** Spring Boot Actuator
- **Documentation:** SpringDoc OpenAPI (Swagger)
- **Build Tool:** Maven
- **Other:** Lombok, MapStruct, Bean Validation

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

## ⚡ Quick Start

### 1. Clone the repository
```bash
git clone <repository-url>
cd sd-course
```

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE shopping_db;
```

### 3. Configure Application

#### Development Environment
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shopping_db
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

#### Production Environment
Set environment variables:
```bash
export DB_URL=jdbc:postgresql://your-prod-db:5432/shopping_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your-production-secret-key
export JWT_EXPIRATION=86400000
```

### 4. Run the Application

#### Development Mode
```bash
mvn spring-boot:run
```

#### Production Mode
```bash
mvn clean package
java -jar -Dspring.profiles.active=prod target/sd-course-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`

### Authentication in Swagger
1. Register/Login to get a JWT token
2. Click "Authorize" in Swagger UI
3. Enter: `Bearer your-jwt-token`
4. Now you can test all authenticated endpoints

## 🔐 Authentication & Authorization

### Register a new user
```bash
POST /api/auth/register
{
  "username": "user123",
  "email": "user@example.com",
  "password": "password123"
}
```

### Login
```bash
POST /api/auth/login
{
  "usernameOrEmail": "user123",
  "password": "password123"
}
```

**Note:** All API endpoints require authentication except:
- Authentication endpoints (`/api/auth/**`)
- API documentation (`/swagger-ui/**`, `/v3/api-docs/**`)

## 📡 API Endpoints

### Authentication (Public)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### User Management (Authenticated)
- `GET /api/users` - List users with pagination (ADMIN only)
- `GET /api/users/list` - Simple user list (ADMIN only)
- `GET /api/users/{id}` - Get user by ID (ADMIN or own profile)
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/{id}` - Update user (ADMIN or own profile)
- `DELETE /api/users/{id}` - Delete user (ADMIN only)
- `POST /api/users/{id}/roles/{roleName}` - Assign role (ADMIN only)
- `DELETE /api/users/{id}/roles/{roleName}` - Remove role (ADMIN only)

### Category Management (Authenticated)
- `GET /api/categories` - List all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category (ADMIN only)
- `PUT /api/categories/{id}` - Update category (ADMIN only)
- `DELETE /api/categories/{id}` - Delete category (ADMIN only)

### Product Management (Authenticated)
- `GET /api/products` - List products with pagination
- `GET /api/products/search` - **Advanced search with filters**
- `GET /api/products/{id}` - Get product by ID
- `GET /api/categories/{categoryId}/products` - Get products by category
- `POST /api/products` - Create product (ADMIN only)
- `PUT /api/products/{id}` - Update product (ADMIN only)
- `DELETE /api/products/{id}` - Delete product (ADMIN only)

### Advanced Product Search
```bash
GET /api/products/search?keyword=laptop&categoryId=1&minPrice=500&maxPrice=2000&inStock=true&page=0&size=10&sort=price,asc
```

### Health Monitoring (Production)
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information

## 🗄️ Database Schema

### Users & Roles
- `users` - User accounts with profile information
- `roles` - User roles (ADMIN, USER)
- `user_roles` - Many-to-many relationship

### Products & Categories
- `categories` - Product categories with hierarchical support
- `products` - Product information with category reference and stock tracking

## 🏗️ Project Structure

```
src/main/java/com/sd_project/sd_course/
├── config/          # Configuration classes
│   ├── CacheConfig.java      # Caching configuration
│   ├── OpenApiConfig.java    # Swagger/OpenAPI setup
│   └── SecurityConfig.java   # Security configuration
├── controller/      # REST controllers
│   ├── AuthController.java   # Authentication endpoints
│   ├── CategoryController.java
│   ├── ProductController.java
│   └── UserController.java   # User management
├── dto/            # Data Transfer Objects
│   ├── request/    # Request DTOs
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── CategoryRequest.java
│   │   ├── ProductRequest.java
│   │   └── UserUpdateRequest.java
│   └── response/   # Response DTOs
│       ├── AuthResponse.java
│       ├── CategoryResponse.java
│       ├── ProductResponse.java
│       └── UserResponse.java
├── entity/         # JPA entities
├── exception/      # Custom exceptions & global handler
├── mapper/         # MapStruct mappers
├── repository/     # JPA repositories with custom queries
├── security/       # Security configuration & JWT
└── service/        # Business logic services
    ├── AuthService.java
    ├── CategoryService.java
    ├── ProductService.java
    └── UserService.java
```

## 🔧 Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar -Dspring.profiles.active=prod target/sd-course-0.0.1-SNAPSHOT.jar
```

### Environment Profiles
- **Development:** Default profile with H2 console and detailed logging
- **Production:** Optimized for production with security headers and monitoring

## 🚀 Deployment

### Environment Variables (Production)
```bash
DB_URL=jdbc:postgresql://localhost:5432/shopping_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your-256-bit-secret-key
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

## 🎯 Key Improvements Made

1. **Enhanced Security**: All endpoints now require authentication
2. **User Management**: Complete user administration system
3. **Advanced Search**: Multi-criteria product search with filtering
4. **Performance**: Caching layer for improved response times
5. **Production Ready**: Environment-based configuration and monitoring
6. **Better Documentation**: Comprehensive API documentation with examples
7. **Role Management**: Dynamic role assignment and management

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
