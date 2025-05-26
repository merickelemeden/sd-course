package com.sd_project.sd_course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new product")
public class ProductCreateRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    @Schema(description = "Product name", example = "iPhone 15 Pro", required = true)
    private String name;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Product description", example = "Latest iPhone with advanced camera system")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 10 integer digits and 2 decimal places")
    @Schema(description = "Product price", example = "999.99", required = true)
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Schema(description = "Available stock quantity", example = "100", required = true)
    private Integer stockQuantity;
    
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    @Schema(description = "Category ID", example = "1", required = true)
    private Long categoryId;
} 