package com.sd_project.sd_course.repository;

import com.sd_project.sd_course.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by name (case-insensitive)
     */
    Optional<Product> findByNameIgnoreCase(String name);

    /**
     * Find all products by category ID
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Find products by category ID (list)
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Find products in stock (stock > 0)
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0")
    Page<Product> findInStockProducts(Pageable pageable);

    /**
     * Find products by price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   Pageable pageable);

    /**
     * Search products by name or description (case-insensitive)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchByNameOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find products by name containing text (case-insensitive)
     */
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Find products by category and in stock
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.stockQuantity > 0")
    Page<Product> findByCategoryIdAndInStock(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Find products with low stock (stock <= threshold)
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * Count products by category
     */
    Long countByCategoryId(Long categoryId);

    /**
     * Check if product exists by name (case-insensitive)
     */
    Boolean existsByNameIgnoreCase(String name);

    /**
     * Find all products ordered by name
     */
    Page<Product> findAllByOrderByNameAsc(Pageable pageable);

    /**
     * Find all products ordered by price
     */
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);

    /**
     * Find all products ordered by creation date (newest first)
     */
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find products by category ID ordered by name
     */
    Page<Product> findByCategoryIdOrderByNameAsc(Long categoryId, Pageable pageable);

    /**
     * Find products with stock quantity greater than specified value
     */
    Page<Product> findByStockQuantityGreaterThan(Integer stockQuantity, Pageable pageable);
} 