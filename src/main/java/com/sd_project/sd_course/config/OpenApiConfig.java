package com.sd_project.sd_course.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shopping Website API",
                description = """
                        A comprehensive RESTful API for a shopping website built with Spring Boot.
                        
                        ## Features
                        - **User Authentication & Authorization** with JWT
                        - **Product Management** with categories
                        - **Role-based Access Control** (ADMIN, USER)
                        - **Comprehensive Error Handling**
                        
                        ## Authentication
                        1. Register a new user or login with existing credentials
                        2. Use the JWT token in the Authorization header: `Bearer <token>`
                        3. Admin users have access to all management operations
                        
                        ## Getting Started
                        1. **Register:** `POST /api/auth/register`
                        2. **Login:** `POST /api/auth/login`
                        3. **Use Token:** Add `Authorization: Bearer <your-token>` to requests
                        """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Shopping Website API Support",
                        email = "support@shopping-website.com",
                        url = "https://shopping-website.com/support"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Server",
                        url = "https://api.shopping-website.com"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Bearer Token Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
} 