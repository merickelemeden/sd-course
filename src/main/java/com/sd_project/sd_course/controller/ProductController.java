package com.sd_project.sd_course.controller;

import com.sd_project.sd_course.dto.request.ProductCreateRequest;
import com.sd_project.sd_course.dto.request.ProductUpdateRequest;
import com.sd_project.sd_course.dto.response.MessageResponse;
import com.sd_project.sd_course.dto.response.ProductResponse;
import com.sd_project.sd_course.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management operations")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve all products with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/products - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> productPage = productService.getAllProducts(pageable);
        return ResponseEntity.ok(productPage.getContent());
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        
        log.info("GET /api/products/{}", id);
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Search products", description = "Search products by name or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size) {
        
        log.info("GET /api/products/search?keyword={}, page: {}, size: {}", keyword, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(productPage.getContent());
    }

    @Operation(summary = "Get products by price range", description = "Retrieve products within a price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price range"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size) {
        
        log.info("GET /api/products/price-range?minPrice={}, maxPrice={}, page: {}, size: {}", 
                minPrice, maxPrice, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(productPage.getContent());
    }

    @Operation(summary = "Get products in stock", description = "Retrieve products that are currently in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponse>> getProductsInStock(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size) {
        
        log.info("GET /api/products/in-stock - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.getProductsInStock(pageable);
        return ResponseEntity.ok(productPage.getContent());
    }

    @Operation(summary = "Get products by category", description = "Retrieve products from a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long categoryId,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size) {
        
        log.info("GET /api/products/category/{} - page: {}, size: {}", categoryId, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(productPage.getContent());
    }

    @Operation(summary = "Create new product", description = "Create a new product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Product creation request", required = true)
            @Valid @RequestBody ProductCreateRequest request) {
        
        log.info("POST /api/products - Creating product: {}", request.getName());
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "Update product", description = "Update an existing product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "Product or category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Product update request", required = true)
            @Valid @RequestBody ProductUpdateRequest request) {
        
        log.info("PUT /api/products/{} - Updating product: {}", id, request.getName());
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Delete product", description = "Delete a product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        
        log.info("DELETE /api/products/{}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
    }

    @Operation(summary = "Update product stock", description = "Update product stock quantity (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid stock quantity"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateStock(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New stock quantity", required = true)
            @RequestParam Integer quantity) {
        
        log.info("PATCH /api/products/{}/stock - Updating stock to: {}", id, quantity);
        ProductResponse product = productService.updateStock(id, quantity);
        return ResponseEntity.ok(product);
    }
} 