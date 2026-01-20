package com.tcs.Library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.HashMap;

/**
 * Response DTO for validation errors.
 * Returns structured error information to the frontend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private boolean success = false;
    private String message;
    private Map<String, String> fieldErrors = new HashMap<>();

    public ValidationErrorResponse(String message, Map<String, String> fieldErrors) {
        this.success = false;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static ValidationErrorResponse of(String message, Map<String, String> errors) {
        return new ValidationErrorResponse(message, errors);
    }
}
