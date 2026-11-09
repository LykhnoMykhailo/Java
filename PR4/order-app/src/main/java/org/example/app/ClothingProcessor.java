package org.example.app;

import org.example.core.exception.OrderProcessingException;
import org.example.core.model.Clothing;
import org.example.core.model.Order;
import org.example.threading.StockRegistry;

/**
 * Concrete clothing processor, invoked from {@link Main} through a bound
 * instance method reference ({@code clothingProcessor::process}).
 */
final class ClothingProcessor {

    private final StockRegistry stock;

    ClothingProcessor(StockRegistry stock) {
        this.stock = stock;
    }

    void process(Order<Clothing> order) throws OrderProcessingException {
        try {
            Thread.sleep(120);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OrderProcessingException("Clothing processing interrupted", e);
        }

        Clothing clothing = order.getProduct();
        if (clothing.getSize() == null || clothing.getSize().isBlank()) {
            throw new OrderProcessingException("Missing size for " + clothing.getName());
        }
        if (!stock.reserve(clothing.getSku(), order.getQuantity())) {
            throw new OrderProcessingException(
                    "Not enough stock for " + clothing.getName()
                            + " (requested " + order.getQuantity()
                            + ", available " + stock.available(clothing.getSku()) + ")");
        }
    }
}
