package com.sd_project.sd_course.mapper;

import com.sd_project.sd_course.dto.request.CategoryCreateRequest;
import com.sd_project.sd_course.dto.request.CategoryUpdateRequest;
import com.sd_project.sd_course.dto.response.CategoryResponse;
import com.sd_project.sd_course.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Map Category entity to CategoryResponse DTO
     */
    @Mapping(target = "productCount", ignore = true) // Will be set manually in service
    CategoryResponse toResponse(Category category);

    /**
     * Map CategoryCreateRequest to Category entity
     */
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryCreateRequest request);

    /**
     * Update Category entity from CategoryUpdateRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CategoryUpdateRequest request, @MappingTarget Category category);

    /**
     * Map Category entity to CategoryResponse with product count
     */
    @Mapping(target = "productCount", source = "productCount")
    CategoryResponse toResponseWithProductCount(Category category, Long productCount);
} 