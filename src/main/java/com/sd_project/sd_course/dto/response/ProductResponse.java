package com.sd_project.sd_course.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product response with details")
public class ProductResponse {
    
    @Schema(description = "Product ID", example = "1")
    private Long id;
    
    @Schema(description = "Product name", example = "iPhone 15 Pro")
    private String name;
    
    @Schema(description = "Product description", example = "Latest iPhone with advanced camera system")
    private String description;
    
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;
    
    @Schema(description = "Available stock quantity", example = "100")
    private Integer stockQuantity;
    
    @Schema(description = "Stock status", example = "IN_STOCK")
    private String stockStatus;
    
    @Schema(description = "Category information")
    private CategoryInfo category;
    
    @Schema(description = "Product creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Product last update timestamp", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Basic category information")
    public static class CategoryInfo {
        @Schema(description = "Category ID", example = "1")
        private Long id;
        
        @Schema(description = "Category name", example = "Electronics")
        private String name;
    }
} 