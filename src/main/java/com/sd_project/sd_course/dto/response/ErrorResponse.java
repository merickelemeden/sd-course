package com.sd_project.sd_course.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error code for client identification", example = "RESOURCE_NOT_FOUND")
    private String error;

    @Schema(description = "Human-readable error message", example = "Category not found with id: 123")
    private String message;

    @Schema(description = "Detailed error description")
    private String details;

    @Schema(description = "API path where error occurred", example = "/api/categories/123")
    private String path;

    @Schema(description = "Error timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "List of validation errors")
    private List<ValidationError> validationErrors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Individual validation error")
    public static class ValidationError {
        @Schema(description = "Field name", example = "name")
        private String field;

        @Schema(description = "Rejected value", example = "")
        private Object rejectedValue;

        @Schema(description = "Error message", example = "Category name is required")
        private String message;
    }

    // Convenience constructors
    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String details, String path) {
        this(status, error, message, path);
        this.details = details;
    }
} 