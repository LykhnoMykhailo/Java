package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Створення категорій

        Category electronics = new Category(1, "Електроніка");

        Category smartphones = new Category(2, "Смартфони");

        Category accessories = new Category(3, "Аксесуари");

        // Створення об'єктів класу Product з вказівкою категорії



        // Створення об'єктів класу Product

        Product product1 = new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics);

        Product product2 = new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном…", smartphones);

        Product product3 = new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories);

        // Список товарів (каталог) для зручного пошуку та додавання
        List<Product> catalog = new ArrayList<>();
        catalog.add(product1);
        catalog.add(product2);
        catalog.add(product3);
        List<Order> orderHistory = new ArrayList<>();
        // Створення кошика
        // Оголошення товарів і категорій з попереднього коду
        Cart cart = new Cart();
        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Переглянути кошик");
            System.out.println("4 - Видалити товар з кошика");
            System.out.println("5 - Зробити замовлення");
            System.out.println("6 - Історія замовлень");
            System.out.println("7 - Пошук товарів");
            System.out.println("0 - Вийти");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    for (Product p : catalog) {
                        System.out.println(p);
                    }
                    break;
                case 2:
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    int idToAdd = scanner.nextInt();
                    boolean foundToAdd = false;
                    for (Product p : catalog) {
                        if (p.getId() == idToAdd) {
                            cart.addProduct(p);
                            System.out.println("Товар додано до кошика.");
                            foundToAdd = true;
                            break;
                        }
                    }
                    if (!foundToAdd) System.out.println("Товар з таким ID не знайдено");
                    break;
                case 3:
                    System.out.println(cart);
                    break;
                case 4:
                    System.out.println("Введіть ID товару для видалення з кошика:");
                    int idToRemove = scanner.nextInt();
                    boolean foundToRemove = false;
                    // Проходимо по товарах, які зараз є в кошику
                    for (Product p : cart.getProducts()) {
                        if (p.getId() == idToRemove) {
                            cart.removeProduct(p);
                            System.out.println("Товар видалено з кошика.");
                            foundToRemove = true;
                            break;
                        }
                    }
                    if (!foundToRemove) System.out.println("Товар з таким ID не знайдено в кошику.");
                    break;
                case 5:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
                    } else {
                        Order order = new Order(cart);
                        orderHistory.add(order); // Збереження замовлення в історію
                        System.out.println("Замовлення оформлено:");
                        System.out.println(order);
                        cart.clear();
                    }
                    break;
                case 6:
                    if (orderHistory.isEmpty()) {
                        System.out.println("Історія замовлень порожня.");
                    } else {
                        System.out.println("--- Історія ваших замовлень ---");
                        for (int i = 0; i < orderHistory.size(); i++) {
                            System.out.println("\nЗамовлення #" + (i + 1) + ":");
                            System.out.println(orderHistory.get(i));
                        }
                    }
                    break;
                case 7:
                    System.out.println("Введіть назву товару або категорії для пошуку:");
                    String query = scanner.nextLine().toLowerCase();
                    boolean foundInSearch = false;
                    for (Product p : catalog) {
                        // Перевіряємо збіг у назві товару або назві категорії
                        if (p.getName().toLowerCase().contains(query) ||
                                p.getCategory().getName().toLowerCase().contains(query)) {
                            System.out.println(p);
                            foundInSearch = true;
                        }
                    }
                    if (!foundInSearch) {
                        System.out.println("Товари за запитом '" + query + "' не знайдено.");
                    }
                    break;

                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;
                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }

        }
    }
}
