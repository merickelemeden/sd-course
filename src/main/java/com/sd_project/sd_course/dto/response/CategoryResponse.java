package com.sd_project.sd_course.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Category response with details")
public class CategoryResponse {
    
    @Schema(description = "Category ID", example = "1")
    private Long id;
    
    @Schema(description = "Category name", example = "Electronics")
    private String name;
    
    @Schema(description = "Category description", example = "Electronic devices and accessories")
    private String description;
    
    @Schema(description = "Number of products in this category", example = "25")
    private Long productCount;
    
    @Schema(description = "Category creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Category last update timestamp", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
} 