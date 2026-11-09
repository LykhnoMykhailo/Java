package org.example.core.exception;

/**
 * Thrown when a value does not match the type parameter expected by a
 * generic operation.
 */
public class ValidationException extends OrderProcessingException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
