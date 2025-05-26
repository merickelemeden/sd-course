package com.sd_project.sd_course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new category")
public class CategoryCreateRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    @Schema(description = "Category name", example = "Electronics", required = true)
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Category description", example = "Electronic devices and accessories")
    private String description;
} 