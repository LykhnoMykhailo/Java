package org.example.app;

import org.example.core.exception.OrderProcessingException;
import org.example.core.exception.ValidationException;
import org.example.core.model.Clothing;
import org.example.core.model.Electronics;
import org.example.core.model.Order;
import org.example.core.model.Orders;
import org.example.core.model.Product;
import org.example.processing.OrderCallback;
import org.example.processing.OrderFilters;
import org.example.processing.OrderProcessor;
import org.example.processing.ProcessingService;
import org.example.storage.OrderRepository;
import org.example.storage.Repositories;
import org.example.threading.OrderStats;
import org.example.threading.ParallelAggregator;
import org.example.threading.StockRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Multithreaded order management system ===");
        System.out.println("(Generics + Lombok @Builder + JavaFaker + JPMS modules)");

        FakerProductFactory factory = new FakerProductFactory();
        StockRegistry stock = new StockRegistry();

        List<Order<Electronics>> electronicsOrders = new ArrayList<>();
        List<Order<Clothing>> clothingOrders = new ArrayList<>();

        // JavaFaker fills the objects, Lombok builders construct them.
        for (int i = 0; i < 6; i++) {
            Electronics product = factory.randomElectronics();
            stock.register(product.getSku(), 2);
            electronicsOrders.add(Orders.create(product, randomQuantity()));
        }
        for (int i = 0; i < 6; i++) {
            Clothing product = factory.randomClothing();
            stock.register(product.getSku(), 3);
            clothingOrders.add(Orders.create(product, randomQuantity()));
        }

        // Deliberately broken orders to exercise exception handling.
        Electronics outOfStock = Electronics.builder()
                .sku("TABLET-03")
                .name("Tablet Air")
                .price(650.0)
                .warrantyMonths(18)
                .build();
        electronicsOrders.add(Orders.create(outOfStock, 1));

        Clothing missingSize = Clothing.builder()
                .sku("HOODIE-09")
                .name("Hoodie")
                .price(55.0)
                .size("")
                .build();
        clothingOrders.add(Orders.create(missingSize, 1));

        OrderRepository<Electronics> electronicsRepository = Repositories.inMemory();
        OrderRepository<Clothing> clothingRepository = Repositories.inMemory();

        // Method references to store orders.
        electronicsOrders.forEach(electronicsRepository::save);
        clothingOrders.forEach(clothingRepository::save);

        List<Order<? extends Product>> allOrders = new ArrayList<>();
        allOrders.addAll(electronicsOrders);
        allOrders.addAll(clothingOrders);

        System.out.println("Generated orders via JavaFaker: " + allOrders.size());

        // Lambda expressions to filter orders by product type.
        long electronicsCount = allOrders.stream()
                .filter(order -> order.getProduct() instanceof Electronics)
                .count();
        long clothingCount = allOrders.stream()
                .filter(order -> order.getProduct() instanceof Clothing)
                .count();
        System.out.println("Filter by type (lambda): electronics=" + electronicsCount
                + ", clothing=" + clothingCount);

        // Generic helper with a bounded type parameter and checked exception.
        List<Order<Electronics>> electronicsOnly = OrderFilters.byType(allOrders, Electronics.class);
        System.out.println("Filter by type (generic method): electronics=" + electronicsOnly.size());

        // Lambda-based processor for electronics.
        OrderProcessor<Electronics> electronicsProcessor = order -> {
            sleep(60);
            if (!stock.reserve(order.getProduct().getSku(), order.getQuantity())) {
                throw new OrderProcessingException(
                        "Out of stock: " + order.getProduct().getName());
            }
        };

        // Method reference to a processing method for clothing.
        ClothingProcessor clothingProcessorImpl = new ClothingProcessor(stock);
        OrderProcessor<Clothing> clothingProcessor = clothingProcessorImpl::process;

        OrderStats stats = new OrderStats();
        OrderCallback<Electronics> electronicsCallback = (order, error) -> record(stats, order, error);
        OrderCallback<Clothing> clothingCallback = (order, error) -> record(stats, order, error);

        boolean allProcessed;
        try (ProcessingService<Electronics> electronicsService =
                     new ProcessingService<>("electronics", 3, electronicsProcessor, electronicsCallback);
             ProcessingService<Clothing> clothingService =
                     new ProcessingService<>("clothing", 2, clothingProcessor, clothingCallback)) {

            electronicsService.start();
            clothingService.start();

            // Method references to submit orders to the worker pools.
            electronicsOrders.forEach(electronicsService::submit);
            clothingOrders.forEach(clothingService::submit);

            boolean electronicsDone = electronicsService.awaitCompletion(15, TimeUnit.SECONDS);
            boolean clothingDone = clothingService.awaitCompletion(15, TimeUnit.SECONDS);
            allProcessed = electronicsDone && clothingDone;
        }

        System.out.println("\nAll submitted orders processed: " + allProcessed);
        System.out.println("Storage sizes: electronics=" + electronicsRepository.size()
                + ", clothing=" + clothingRepository.size());
        System.out.println("Stats: " + stats.snapshot());

        double parallelRevenue = ParallelAggregator.totalRevenue(allOrders);
        long parallelCompleted = ParallelAggregator.countCompleted(allOrders);
        System.out.printf("Parallel aggregator: completed=%d, revenue=%.2f%n",
                parallelCompleted, parallelRevenue);

        System.out.println("\n--- Order results ---");
        allOrders.stream()
                .map(order -> String.format("%-7s %-38s %-10s %10.2f %s",
                        order.getId(),
                        truncate(order.getProduct().getName(), 38),
                        order.getStatus(),
                        order.total(),
                        order.getError() == null ? "" : "(" + order.getError() + ")"))
                .forEach(System.out::println);

        System.out.println("\n--- Product names (method references) ---");
        allOrders.stream()
                .map(Order::getProduct)
                .map(Product::getName)
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .forEach(System.out::println);

        demonstrateGenericException();
    }

    private static int randomQuantity() {
        return ThreadLocalRandom.current().nextInt(1, 3);
    }

    private static String truncate(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max - 1) + "~";
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static <T extends Product> void record(OrderStats stats, Order<T> order, Throwable error) {
        if (error == null) {
            stats.recordCompleted(order.getProduct().category(), order.total());
        } else {
            stats.recordFailed(order.getProduct().category());
        }
    }

    private static void demonstrateGenericException() {
        System.out.println("\n--- Generic type validation ---");
        try {
            Product product = Clothing.builder()
                    .sku("MISC-01")
                    .name("Mystery Product")
                    .price(10.0)
                    .size("M")
                    .build();
            Electronics electronics = requireType(product, Electronics.class);
            System.out.println("Unexpected success: " + electronics);
        } catch (ValidationException e) {
            System.out.println("Handled generic type error: " + e.getMessage());
        }
    }

    /**
     * Generic method with a bounded type parameter and a Class token. The
     * ClassCastException risk is handled explicitly.
     */
    private static <T extends Product> T requireType(Product product, Class<T> type)
            throws ValidationException {
        if (!type.isInstance(product)) {
            throw new ValidationException(
                    "Expected " + type.getSimpleName()
                            + " but got " + product.getClass().getSimpleName());
        }
        try {
            return type.cast(product);
        } catch (ClassCastException e) {
            throw new ValidationException("Generic cast failed", e);
        }
    }
}
