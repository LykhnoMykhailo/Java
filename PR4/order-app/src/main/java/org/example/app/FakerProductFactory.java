package org.example.app;

import com.github.javafaker.Faker;
import org.example.core.model.Clothing;
import org.example.core.model.Electronics;

import java.util.Locale;

/**
 * Populates product objects with random data from JavaFaker and creates them
 * through the Lombok-generated builders.
 */
public final class FakerProductFactory {

    private final Faker faker;

    public FakerProductFactory() {
        this(new Faker(Locale.ENGLISH));
    }

    public FakerProductFactory(Faker faker) {
        this.faker = faker;
    }

    public Electronics randomElectronics() {
        return Electronics.builder()
                .sku(randomSku("EL"))
                .name(faker.commerce().productName())
                .price(randomPrice())
                .warrantyMonths(faker.number().numberBetween(6, 36))
                .build();
    }

    public Clothing randomClothing() {
        return Clothing.builder()
                .sku(randomSku("CL"))
                .name(faker.commerce().productName())
                .price(randomPrice())
                .size(faker.options().option("XS", "S", "M", "L", "XL", "XXL"))
                .build();
    }

    private String randomSku(String prefix) {
        return prefix + "-" + faker.bothify("??####").toUpperCase(Locale.ROOT);
    }

    private double randomPrice() {
        double value = faker.number().randomDouble(2, 10, 2000);
        return Math.round(value * 100.0) / 100.0;
    }
}
