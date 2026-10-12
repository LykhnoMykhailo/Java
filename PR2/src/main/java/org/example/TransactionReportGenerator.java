package org.example;

import java.util.List;
import java.util.Map;

public abstract class TransactionReportGenerator {

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("\n10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printMaxAndMinExpensesReport(String monthYear, Map<String, Transaction> maxMinMap) {
        System.out.println("\nНайбільші та найменші витрати за період " + monthYear + ":");
        if (maxMinMap.isEmpty()) {
            System.out.println("Немає даних за цей період.");
            return;
        }
        Transaction max = maxMinMap.get("max");
        Transaction min = maxMinMap.get("min");
        if (max != null) {
            System.out.println("  Найбільша витрата: " + max.getDescription() + " (" + max.getAmount() + " грн)");
        }
        if (min != null) {
            System.out.println("  Найменша витрата: " + min.getDescription() + " (" + min.getAmount() + " грн)");
        }
    }

    // Самостійна робота: Текстовий звіт з візуалізацією символом '*' (1 зірочка = 1000 грн)
    public static void printVisualReport(String title, Map<String, Double> data) {
        System.out.println("\n" + title + " (1 символ '*' = 1000 грн):");
        data.forEach((key, value) -> {
            int starsCount = (int) (value / 1000);
            String stars = "*".repeat(Math.max(0, starsCount));
            System.out.printf("  %-25s | %.2f грн %s\n", key, value, stars);
        });
    }
}