package com.sd_project.sd_course.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    private Set<String> roles;
} 