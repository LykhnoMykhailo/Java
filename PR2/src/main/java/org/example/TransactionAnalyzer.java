package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM-yyyy");

    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate(), DATE_FORMAT);
            String transactionMonthYear = date.format(MONTH_FORMAT);
            if (transactionMonthYear.equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount)) // від найменших (найбільших за модулем)
                .limit(10)
                .collect(Collectors.toList());
    }

    // Самостійна робота: Визначення найбільших і найменших витрат за вказаний період (MM-yyyy)
    public static Map<String, Transaction> findMaxAndMinExpensesForPeriod(List<Transaction> transactions, String monthYear) {
        List<Transaction> periodExpenses = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = LocalDate.parse(t.getDate(), DATE_FORMAT);
                    return date.format(MONTH_FORMAT).equals(monthYear);
                })
                .collect(Collectors.toList());

        if (periodExpenses.isEmpty()) {
            return new HashMap<>();
        }

        // Найбільша витрата (найменше від'ємне число, наприклад -9100)
        Transaction maxExpense = periodExpenses.stream()
                .min(Comparator.comparing(Transaction::getAmount))
                .orElse(null);

        // Найменша витрата (найближче до 0 від'ємне число, наприклад -50)
        Transaction minExpense = periodExpenses.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(null);

        Map<String, Transaction> result = new HashMap<>();
        result.put("max", maxExpense);
        result.put("min", minExpense);
        return result;
    }

    // Сумарні витрати по категоріях для звітів
    public static Map<String, Double> getExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        Collectors.summingDouble(t -> Math.abs(t.getAmount()))
                ));
    }

    // Сумарні витрати по місяцях для звітів
    public static Map<String, Double> getExpensesByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> LocalDate.parse(t.getDate(), DATE_FORMAT).format(MONTH_FORMAT),
                        Collectors.summingDouble(t -> Math.abs(t.getAmount()))
                ));
    }
}