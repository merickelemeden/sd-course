# Shopping Website - Setup Instructions

## Prerequisites

1. **Java 21** - Make sure you have Java 21 installed
2. **Maven 3.6+** - For dependency management and building
3. **PostgreSQL 12+** - Database server
4. **Git** - Version control

## Database Setup

### 1. Install PostgreSQL
- Download and install PostgreSQL from [https://www.postgresql.org/download/](https://www.postgresql.org/download/)
- Make sure PostgreSQL service is running

### 2. Create Database
```sql
-- Connect to PostgreSQL as postgres user
psql -U postgres

-- Create database
CREATE DATABASE shopping_db;

-- Create user (optional, you can use postgres user)
CREATE USER shopping_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE shopping_db TO shopping_user;

-- Exit psql
\q
```

### 3. Initialize Database Schema
```bash
# Navigate to project directory
cd /path/to/your/project

# Run the initialization script
psql -U postgres -d shopping_db -f src/main/resources/database-init.sql
```

## Application Configuration

### 1. Update Database Connection
Edit `src/main/resources/application.properties` if needed:
```properties
# Update these values according to your PostgreSQL setup
spring.datasource.url=jdbc:postgresql://localhost:5432/shopping_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 2. JWT Secret Key
For production, change the JWT secret in `application.properties`:
```properties
# Use a secure, random secret key (minimum 256 bits)
app.jwt.secret=your-secure-secret-key-here
```

## Running the Application

### 1. Build the Project
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package
```

### 2. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Or using the packaged JAR
java -jar target/sd-course-0.0.1-SNAPSHOT.jar
```

### 3. Access the Application
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Categories (Public read, Admin write)
- `GET /api/categories` - List all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category (Admin only)
- `PUT /api/categories/{id}` - Update category (Admin only)
- `DELETE /api/categories/{id}` - Delete category (Admin only)

### Products (Public read, Admin write)
- `GET /api/products` - List all products with pagination
- `GET /api/products/{id}` - Get product by ID
- `GET /api/categories/{categoryId}/products` - Get products by category
- `POST /api/products` - Create product (Admin only)
- `PUT /api/products/{id}` - Update product (Admin only)
- `DELETE /api/products/{id}` - Delete product (Admin only)

## Development Guidelines

### 1. Code Structure
Follow the established package structure:
```
src/main/java/com/sd_project/sd_course/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Custom exceptions
├── mapper/         # MapStruct mappers
├── repository/     # JPA repositories
├── security/       # Security components
└── service/        # Business logic
```

### 2. Naming Conventions
- **Classes**: PascalCase (e.g., `UserService`)
- **Methods**: camelCase (e.g., `findByUsername`)
- **Variables**: camelCase (e.g., `userId`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_PAGE_SIZE`)
- **REST endpoints**: kebab-case (e.g., `/api/categories`)

### 3. Testing
- Write unit tests for services
- Write integration tests for repositories
- Write controller tests with MockMvc
- Maintain test coverage above 80%

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check if PostgreSQL is running
   - Verify database name, username, and password
   - Ensure database exists

2. **Port Already in Use**
   - Change server port in `application.properties`
   - Or kill the process using port 8080

3. **JWT Token Issues**
   - Check if the secret key is properly configured
   - Verify token expiration settings

4. **Build Failures**
   - Ensure Java 21 is installed and configured
   - Check Maven configuration
   - Verify all dependencies are available

### Logs
Check application logs for detailed error information:
```bash
# Application logs will be displayed in the console
# Look for ERROR and WARN level messages
```

## Next Development Steps

1. Implement Phase 2: Database Design & Entities
2. Create JPA entities with proper relationships
3. Implement Phase 3: Security with JWT
4. Build REST controllers and services
5. Add comprehensive exception handling
6. Integrate Swagger documentation
7. Write tests for all components

## Production Deployment

Before deploying to production:

1. Change default passwords and secrets
2. Configure proper logging levels
3. Set up database connection pooling
4. Configure HTTPS/SSL
5. Set up monitoring and health checks
6. Configure backup strategies 