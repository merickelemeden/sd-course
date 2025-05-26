package com.sd_project.sd_course.controller;

import com.sd_project.sd_course.dto.request.CategoryCreateRequest;
import com.sd_project.sd_course.dto.request.CategoryUpdateRequest;
import com.sd_project.sd_course.dto.response.CategoryResponse;
import com.sd_project.sd_course.dto.response.MessageResponse;
import com.sd_project.sd_course.service.CategoryService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management operations")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Retrieve all categories with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @Parameter(description = "Enable pagination")
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "10") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/categories - paginated: {}, page: {}, size: {}", paginated, page, size);
        
        if (paginated) {
            Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<CategoryResponse> categoryPage = categoryService.getAllCategories(pageable);
            return ResponseEntity.ok(categoryPage.getContent());
        } else {
            List<CategoryResponse> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        }
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id) {
        
        log.info("GET /api/categories/{}", id);
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Search categories", description = "Search categories by name or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategories(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        
        log.info("GET /api/categories/search?keyword={}", keyword);
        List<CategoryResponse> categories = categoryService.searchCategories(keyword);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Create new category", description = "Create a new category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "409", description = "Category name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @Parameter(description = "Category creation request", required = true)
            @Valid @RequestBody CategoryCreateRequest request) {
        
        log.info("POST /api/categories - Creating category: {}", request.getName());
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "Update category", description = "Update an existing category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Category name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Category update request", required = true)
            @Valid @RequestBody CategoryUpdateRequest request) {
        
        log.info("PUT /api/categories/{} - Updating category: {}", id, request.getName());
        CategoryResponse category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Delete category", description = "Delete a category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete category with existing products"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable Long id) {
        
        log.info("DELETE /api/categories/{}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
    }
} 