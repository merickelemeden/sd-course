package com.sd_project.sd_course.entity;

import com.sd_project.sd_course.exception.InsufficientStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Min(0)
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }

    public boolean hasStock(int quantity) {
        return stockQuantity != null && stockQuantity >= quantity;
    }

    public void reduceStock(int quantity) {
        if (!hasStock(quantity)) {
            throw new InsufficientStockException(getId(), stockQuantity, quantity);
        }
        this.stockQuantity -= quantity;
    }

    public void addStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot add negative stock quantity");
        }
        this.stockQuantity = (this.stockQuantity == null ? 0 : this.stockQuantity) + quantity;
    }

    public void removeStock(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!hasStock(quantity)) {
            throw new InsufficientStockException(getId(), stockQuantity, quantity);
        }
        this.stockQuantity -= quantity;
    }

    public StockStatus getStockStatus() {
        if (stockQuantity == null || stockQuantity == 0) {
            return StockStatus.OUT_OF_STOCK;
        } else if (stockQuantity <= 10) {
            return StockStatus.LOW_STOCK;
        } else {
            return StockStatus.IN_STOCK;
        }
    }

    public enum StockStatus {
        IN_STOCK,
        LOW_STOCK,
        OUT_OF_STOCK
    }
} 