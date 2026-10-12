package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalance() {
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-01-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        Assertions.assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("15-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-02-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int result = TransactionAnalyzer.countTransactionsByMonth(transactions, "01-2023");

        Assertions.assertEquals(2, result, "Підрахунок транзакцій за місяць неправильний");
    }

    // Тест для самостійної роботи: перевірка читання з ресурсів CSV
    @Test
    public void testReadResourceCSV() {
        List<Transaction> transactions = TransactionCSVReader.readResource("/pr.csv");
        Assertions.assertNotNull(transactions, "Список транзакцій не повинен бути null");
        Assertions.assertFalse(transactions.isEmpty(), "Список транзакцій з файлу pr.csv не повинен бути порожнім");
    }

    // Тест для самостійної роботи: перевірка пошуку найбільших витрат
    @Test
    public void testFindTopExpenses() {
        Transaction t1 = new Transaction("01-01-2023", -100.0, "Їжа");
        Transaction t2 = new Transaction("02-01-2023", -500.0, "Техніка");
        Transaction t3 = new Transaction("03-01-2023", 1000.0, "Зарплата");
        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(2, topExpenses.size(), "Доступно лише 2 витрати (від'ємні суми)");
        Assertions.assertEquals(-500.0, topExpenses.get(0).getAmount(), "Найбільша витрата має бути першою");
    }
}