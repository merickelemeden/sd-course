# Spring Boot Shopping Website 🛒

A RESTful shopping website built with Spring Boot, featuring categories and products management, role-based security, and JWT authentication.

## 🚀 Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (ADMIN, USER)
  - Secure user registration and login

- **Product Management**
  - CRUD operations for products
  - Category-based product organization
  - Stock quantity tracking
  - Pagination and filtering

- **Category Management**
  - CRUD operations for categories
  - Hierarchical category structure

- **Security**
  - Spring Security integration
  - JWT token-based authentication
  - Protected endpoints for admin operations
  - Public endpoints for browsing products

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3.5.0, Java 21
- **Database:** PostgreSQL
- **Security:** Spring Security, JWT
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
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shopping_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`

## 🔐 Authentication

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

## 📡 API Endpoints

### Public Endpoints
- `GET /api/categories` - List all categories
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/products` - List all products (with pagination)
- `GET /api/products/{id}` - Get product by ID
- `GET /api/categories/{categoryId}/products` - Get products by category

### Admin Only Endpoints
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category
- `POST /api/products` - Create product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

## 🗄️ Database Schema

### Users & Roles
- `users` - User accounts
- `roles` - User roles (ADMIN, USER)
- `user_roles` - Many-to-many relationship

### Products & Categories
- `categories` - Product categories
- `products` - Product information with category reference

## 🏗️ Project Structure

```
src/main/java/com/sd_project/sd_course/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
│   ├── request/    # Request DTOs
│   └── response/   # Response DTOs
├── entity/         # JPA entities
├── exception/      # Custom exceptions & global handler
├── mapper/         # MapStruct mappers
├── repository/     # JPA repositories
├── security/       # Security configuration & JWT
└── service/        # Business logic services
```

## 🔧 Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/sd-course-0.0.1-SNAPSHOT.jar
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

For questions or support, please open an issue in the GitHub repository.

---

**Happy Shopping! 🛍️** 