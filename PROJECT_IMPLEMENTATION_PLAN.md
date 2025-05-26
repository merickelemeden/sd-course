# Spring MVC Shopping Website - Implementation Plan

## Project Overview
Building a RESTful shopping website with Spring Boot, featuring categories and products management, role-based security, and comprehensive documentation.

## Current Project Status
- ✅ Spring Boot 3.5.0 project initialized
- ✅ Basic dependencies: Spring Web, Spring Security, PostgreSQL, Lombok
- ✅ Java 21 configured

## Implementation Steps

### Phase 1: Project Setup & Dependencies ✅
- [x] Update `pom.xml` with required dependencies
  - Spring Data JPA
  - JWT authentication libraries
  - Swagger/OpenAPI documentation
  - Bean Validation
  - MapStruct for mapping
- [x] Configure application properties
- [x] Set up database connection

### Phase 2: Database Design & Entities ✅
- [x] Design database schema
- [x] Create JPA entities:
  - `User` (for authentication)
  - `Role` (ADMIN, USER)
  - `Category`
  - `Product`
- [x] Set up entity relationships
- [x] Create database migration scripts

### Phase 3: Security Implementation ✅
- [x] Configure Spring Security
- [x] Implement JWT-based authentication
- [x] Create authentication controller (login/register)
- [x] Implement role-based authorization
- [x] Add security configurations for different endpoints

### Phase 4: Core Business Logic ✅
- [x] Implement Repository layer
- [x] Create Service layer with business logic
- [x] Implement DTOs and mappers
- [x] Add comprehensive validation

### Phase 5: REST API Controllers
- [x] Category Controller:
  - `GET /api/categories` - List all categories (public)
  - `GET /api/categories/{id}` - Get category by ID (public)
  - `POST /api/categories` - Create category (ADMIN only)
  - `PUT /api/categories/{id}` - Update category (ADMIN only)
  - `DELETE /api/categories/{id}` - Delete category (ADMIN only)
- [x] Product Controller:
  - `GET /api/products` - List all products with pagination (public)
  - `GET /api/products/{id}` - Get product by ID (public)
  - `GET /api/categories/{categoryId}/products` - Get products by category (public)
  - `POST /api/products` - Create product (ADMIN only)
  - `PUT /api/products/{id}` - Update product (ADMIN only)
  - `DELETE /api/products/{id}` - Delete product (ADMIN only)

### Phase 6: Exception Handling ✅
- [x] Create custom exception classes
- [x] Implement global exception handler with `@ControllerAdvice`
- [x] Standardize error response format
- [x] Add validation error handling

### Phase 7: API Documentation
- [ ] Configure Swagger/OpenAPI
- [ ] Add comprehensive API documentation
- [ ] Document security requirements
- [ ] Add example requests/responses

### Phase 8: Testing
- [ ] Unit tests for services
- [ ] Integration tests for repositories
- [ ] Controller tests with MockMvc
- [ ] Security tests

### Phase 9: Additional Features
- [ ] Add search functionality for products
- [ ] Implement pagination and sorting
- [ ] Add product images support
- [ ] Implement audit logging

## Detailed Technical Specifications

### Database Schema

```sql
-- Users table for authentication
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Roles table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

-- User roles junction table
CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

-- Categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    category_id BIGINT REFERENCES categories(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Project Structure
```
src/main/java/com/sd_project/sd_course/
├── SdCourseApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   └── SwaggerConfig.java
├── controller/
│   ├── AuthController.java
│   ├── CategoryController.java
│   └── ProductController.java
├── dto/
│   ├── request/
│   │   ├── CategoryCreateRequest.java
│   │   ├── CategoryUpdateRequest.java
│   │   ├── ProductCreateRequest.java
│   │   ├── ProductUpdateRequest.java
│   │   └── LoginRequest.java
│   └── response/
│       ├── CategoryResponse.java
│       ├── ProductResponse.java
│       ├── JwtResponse.java
│       └── ErrorResponse.java
├── entity/
│   ├── User.java
│   ├── Role.java
│   ├── Category.java
│   └── Product.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── UnauthorizedException.java
├── mapper/
│   ├── CategoryMapper.java
│   └── ProductMapper.java
├── repository/
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── CategoryRepository.java
│   └── ProductRepository.java
├── security/
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── UserPrincipal.java
└── service/
    ├── AuthService.java
    ├── UserService.java
    ├── CategoryService.java
    └── ProductService.java
```

### Security Requirements
- JWT-based stateless authentication
- Role-based access control (RBAC)
- Public endpoints for reading data
- Protected endpoints for CUD operations (ADMIN only)
- Proper password encoding
- Token expiration and refresh mechanism

### API Design Principles
- RESTful conventions
- Consistent response format
- Proper HTTP status codes
- Comprehensive error messages
- Input validation
- Pagination for list endpoints

### Exception Handling Strategy
- Global exception handler
- Standardized error response format
- Specific exception types for different scenarios
- Proper logging of errors
- Client-friendly error messages

### Documentation Requirements
- Swagger UI integration
- Comprehensive API documentation
- Request/response examples
- Authentication documentation
- Error code documentation

## Next Steps
1. Start with Phase 1: Update dependencies in `pom.xml`
2. Configure application properties
3. Set up database schema
4. Implement entities and repositories
5. Build security layer
6. Create REST controllers
7. Add exception handling
8. Integrate Swagger documentation
9. Write comprehensive tests

## Dependencies to Add
```xml
<!-- Additional dependencies needed -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>1.5.5.Final</version>
    <scope>provided</scope>
</dependency>
```

This plan provides a comprehensive roadmap for building a professional-grade Spring MVC shopping website with all the requested features. Each phase builds upon the previous one, ensuring a systematic and maintainable development process. 