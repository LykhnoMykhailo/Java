package org.example.core.exception;

/**
 * Checked exception raised when an order cannot be processed.
 */
public class OrderProcessingException extends Exception {

    public OrderProcessingException(String message) {
        super(message);
    }

    public OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
