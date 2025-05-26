package com.sd_project.sd_course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd_project.sd_course.dto.response.ProductResponse;
import com.sd_project.sd_course.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllProducts_ShouldReturnProductList() throws Exception {
        // Given
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stockQuantity(10)
                .stockStatus("IN_STOCK")
                .category(ProductResponse.CategoryInfo.builder()
                        .id(1L)
                        .name("Electronics")
                        .build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Page<ProductResponse> productPage = new PageImpl<>(List.of(product), PageRequest.of(0, 20), 1);
        when(productService.getAllProducts(any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[0].stockStatus").value("IN_STOCK"));
    }

    @Test
    @WithMockUser
    void getProductById_ShouldReturnProduct() throws Exception {
        // Given
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .stockQuantity(10)
                .stockStatus("IN_STOCK")
                .build();

        when(productService.getProductById(1L)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    @WithMockUser
    void searchProducts_ShouldReturnFilteredProducts() throws Exception {
        // Given
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .name("iPhone 15")
                .price(new BigDecimal("999.99"))
                .stockQuantity(5)
                .stockStatus("IN_STOCK")
                .build();

        Page<ProductResponse> productPage = new PageImpl<>(List.of(product), PageRequest.of(0, 20), 1);
        when(productService.searchProducts(any(), any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "iPhone")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("iPhone 15"));
    }
} 