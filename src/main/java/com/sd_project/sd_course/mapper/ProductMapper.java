package com.sd_project.sd_course.mapper;

import com.sd_project.sd_course.dto.request.ProductCreateRequest;
import com.sd_project.sd_course.dto.request.ProductUpdateRequest;
import com.sd_project.sd_course.dto.response.ProductResponse;
import com.sd_project.sd_course.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Map Product entity to ProductResponse DTO
     */
    @Mapping(target = "stockStatus", expression = "java(product.getStockStatus().name())")
    @Mapping(target = "category.id", source = "category.id")
    @Mapping(target = "category.name", source = "category.name")
    ProductResponse toResponse(Product product);

    /**
     * Map ProductCreateRequest to Product entity
     */
    @Mapping(target = "category", ignore = true) // Will be set manually in service
    Product toEntity(ProductCreateRequest request);

    /**
     * Update Product entity from ProductUpdateRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Will be set manually in service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

    /**
     * Map Category info for ProductResponse
     */
    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    ProductResponse.CategoryInfo toCategoryInfo(com.sd_project.sd_course.entity.Category category);
} 