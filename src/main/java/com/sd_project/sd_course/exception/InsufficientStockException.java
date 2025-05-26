package com.sd_project.sd_course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

    private Long productId;
    private Integer availableStock;
    private Integer requestedQuantity;

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(Long productId, Integer availableStock, Integer requestedQuantity) {
        super(String.format("Insufficient stock for product ID %d. Available: %d, Requested: %d", 
                productId, availableStock, requestedQuantity));
        this.productId = productId;
        this.availableStock = availableStock;
        this.requestedQuantity = requestedQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }
} 