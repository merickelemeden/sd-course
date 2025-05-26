package com.sd_project.sd_course.service;

import com.sd_project.sd_course.dto.request.CategoryCreateRequest;
import com.sd_project.sd_course.dto.request.CategoryUpdateRequest;
import com.sd_project.sd_course.dto.response.CategoryResponse;
import com.sd_project.sd_course.entity.Category;
import com.sd_project.sd_course.exception.ConflictException;
import com.sd_project.sd_course.exception.ResourceNotFoundException;
import com.sd_project.sd_course.mapper.CategoryMapper;
import com.sd_project.sd_course.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        log.debug("Fetching all categories");
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        log.debug("Fetching categories with pagination: {}", pageable);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(this::mapToResponse);
    }

    public CategoryResponse getCategoryById(Long id) {
        log.debug("Fetching category by id: {}", id);
        Category category = findCategoryById(id);
        return mapToResponse(category);
    }

    public List<CategoryResponse> searchCategories(String keyword) {
        log.debug("Searching categories with keyword: {}", keyword);
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCaseOrderByNameAsc(keyword);
        return categories.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        log.debug("Creating category with name: {}", request.getName());
        
        // Check if category with same name already exists
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", savedCategory.getId());
        
        return mapToResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        log.debug("Updating category with id: {}", id);
        
        Category category = findCategoryById(id);
        
        // Check if another category with same name exists (excluding current category)
        if (!category.getName().equalsIgnoreCase(request.getName()) && 
            categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Category with name '" + request.getName() + "' already exists");
        }

        categoryMapper.updateEntityFromRequest(request, category);

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with id: {}", updatedCategory.getId());
        
        return mapToResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.debug("Deleting category with id: {}", id);
        
        Category category = findCategoryById(id);
        
        // Check if category has products
        long productCount = categoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new ConflictException("Cannot delete category with existing products. Found " + productCount + " products.");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully with id: {}", id);
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    // Helper methods
    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private CategoryResponse mapToResponse(Category category) {
        long productCount = categoryRepository.countProductsByCategoryId(category.getId());
        return categoryMapper.toResponseWithProductCount(category, productCount);
    }
} 