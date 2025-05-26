package com.sd_project.sd_course.service;

import com.sd_project.sd_course.dto.request.ProductCreateRequest;
import com.sd_project.sd_course.dto.request.ProductUpdateRequest;
import com.sd_project.sd_course.dto.response.ProductResponse;
import com.sd_project.sd_course.entity.Category;
import com.sd_project.sd_course.entity.Product;
import com.sd_project.sd_course.exception.ResourceNotFoundException;
import com.sd_project.sd_course.mapper.ProductMapper;
import com.sd_project.sd_course.repository.CategoryRepository;
import com.sd_project.sd_course.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.debug("Fetching products with pagination: {}", pageable);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(this::mapToResponse);
    }

    public ProductResponse getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);
        Product product = findProductById(id);
        return mapToResponse(product);
    }

    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        log.debug("Fetching products by category id: {} with pagination: {}", categoryId, pageable);
        
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }
        
        Page<Product> productPage = productRepository.findByCategoryIdOrderByNameAsc(categoryId, pageable);
        return productPage.map(this::mapToResponse);
    }

    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        log.debug("Searching products with keyword: {} and pagination: {}", keyword, pageable);
        Page<Product> productPage = productRepository.searchByNameOrDescription(keyword, pageable);
        return productPage.map(this::mapToResponse);
    }

    public Page<ProductResponse> advancedSearchProducts(String keyword, Long categoryId, 
                                                       BigDecimal minPrice, BigDecimal maxPrice, 
                                                       Boolean inStock, Pageable pageable) {
        log.debug("Advanced search - keyword: {}, categoryId: {}, minPrice: {}, maxPrice: {}, inStock: {}", 
                keyword, categoryId, minPrice, maxPrice, inStock);
        
        Page<Product> productPage = productRepository.findWithFilters(
                keyword, categoryId, minPrice, maxPrice, inStock, pageable);
        return productPage.map(this::mapToResponse);
    }

    public Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.debug("Fetching products by price range: {} - {} with pagination: {}", minPrice, maxPrice, pageable);
        Page<Product> productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return productPage.map(this::mapToResponse);
    }

    public Page<ProductResponse> getProductsInStock(Pageable pageable) {
        log.debug("Fetching products in stock with pagination: {}", pageable);
        Page<Product> productPage = productRepository.findByStockQuantityGreaterThan(0, pageable);
        return productPage.map(this::mapToResponse);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        log.debug("Creating product with name: {}", request.getName());
        
        // Verify category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        
        return mapToResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        log.debug("Updating product with id: {}", id);
        
        Product product = findProductById(id);
        
        // Verify category exists if changed
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        productMapper.updateEntityFromRequest(request, product);
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.debug("Deleting product with id: {}", id);
        
        Product product = findProductById(id);
        productRepository.delete(product);
        
        log.info("Product deleted successfully with id: {}", id);
    }

    @Transactional
    public ProductResponse updateStock(Long id, Integer quantity) {
        log.debug("Updating stock for product id: {} to quantity: {}", id, quantity);
        
        Product product = findProductById(id);
        product.setStockQuantity(quantity);
        
        Product updatedProduct = productRepository.save(product);
        log.info("Stock updated successfully for product id: {}", id);
        
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse addStock(Long id, Integer quantity) {
        log.debug("Adding stock for product id: {} with quantity: {}", id, quantity);
        
        Product product = findProductById(id);
        product.addStock(quantity);
        
        Product updatedProduct = productRepository.save(product);
        log.info("Stock added successfully for product id: {}", id);
        
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse removeStock(Long id, Integer quantity) {
        log.debug("Removing stock for product id: {} with quantity: {}", id, quantity);
        
        Product product = findProductById(id);
        product.removeStock(quantity);
        
        Product updatedProduct = productRepository.save(product);
        log.info("Stock removed successfully for product id: {}", id);
        
        return mapToResponse(updatedProduct);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    // Helper methods
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    private ProductResponse mapToResponse(Product product) {
        return productMapper.toResponse(product);
    }
} 