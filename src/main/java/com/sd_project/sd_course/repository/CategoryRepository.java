package com.sd_project.sd_course.repository;

import com.sd_project.sd_course.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by name (case-insensitive)
     */
    Optional<Category> findByNameIgnoreCase(String name);

    /**
     * Check if category exists by name (case-insensitive)
     */
    Boolean existsByNameIgnoreCase(String name);

    /**
     * Find categories with products count
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithProducts(@Param("id") Long id);

    /**
     * Find all categories ordered by name
     */
    List<Category> findAllByOrderByNameAsc();

    /**
     * Search categories by name containing the given text (case-insensitive)
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    /**
     * Find categories by name containing keyword (case-insensitive) ordered by name
     */
    List<Category> findByNameContainingIgnoreCaseOrderByNameAsc(String keyword);

    /**
     * Count products in a category
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    Long countProductsByCategoryId(@Param("categoryId") Long categoryId);
} 