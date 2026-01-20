package com.tcs.Library.error;

import java.util.Map;
import java.util.HashMap;
import lombok.Getter;

/**
 * Exception thrown when user registration validation fails.
 * Contains detailed field-level error messages.
 */
@Getter
public class ValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super("Validation failed");
        this.fieldErrors = fieldErrors;
    }

    public void addFieldError(String field, String message) {
        this.fieldErrors.put(field, message);
    }

    public boolean hasErrors() {
        return !fieldErrors.isEmpty();
    }
}
