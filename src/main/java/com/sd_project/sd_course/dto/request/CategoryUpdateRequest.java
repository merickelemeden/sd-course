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
@Schema(description = "Request to update an existing category")
public class CategoryUpdateRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    @Schema(description = "Updated category name", example = "Electronics & Gadgets", required = true)
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Updated category description", example = "Electronic devices, gadgets and accessories")
    private String description;
} 