package org.example;

import java.util.List;
import java.util.Map;

import static org.example.TransactionAnalyzer.*;
import static org.example.TransactionReportGenerator.*;

public class Main {
    public static void main(String[] args) {
        // Читання файлу з локальних ресурсів (вимоги практичної)
        List<Transaction> transactions = TransactionCSVReader.readResource("/pr.csv");

        // 1. Базові звіти з презентації
        double totalBalance = calculateTotalBalance(transactions);
        printBalanceReport(totalBalance);

        String monthYear = "01-2024";
        int transactionsCount = countTransactionsByMonth(transactions, monthYear);
        printTransactionsCountByMonth(monthYear, transactionsCount);

        List<Transaction> topExpenses = findTopExpenses(transactions);
        printTopExpensesReport(topExpenses);

        // 2. Виконання самостійної роботи:
        // - Найбільші та найменші витрати за період
        Map<String, Transaction> maxMin = findMaxAndMinExpensesForPeriod(transactions, monthYear);
        printMaxAndMinExpensesReport(monthYear, maxMin);

        // - Текстові звіти з візуалізацією зірочками (*) по категоріях та місяцях
        Map<String, Double> byCategory = getExpensesByCategory(transactions);
        printVisualReport("Сумарні витрати по категоріях", byCategory);

        Map<String, Double> byMonth = getExpensesByMonth(transactions);
        printVisualReport("Сумарні витрати по місяцях", byMonth);
    }
}