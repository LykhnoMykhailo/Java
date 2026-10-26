package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        try {
            IO.print("Введіть перше число: ");
            double firstNumber = scanner.nextDouble();

            IO.print("Введіть друге число: ");
            double secondNumber = scanner.nextDouble();

            IO.print("Введіть операцію (+, -, *, /): ");
            String operation = scanner.next();

            double result = switch (operation) {
                case "+" -> calculator.add(firstNumber, secondNumber);
                case "-" -> calculator.subtract(firstNumber, secondNumber);
                case "*" -> calculator.multiply(firstNumber, secondNumber);
                case "/" -> calculator.divide(firstNumber, secondNumber);
                default -> throw new InvalidInputException("невідома операція \"" + operation + "\"");
            };

            IO.println("Результат: " + result);
        } catch (InputMismatchException e) {
            IO.println("Помилка: потрібно ввести число.");
        } catch (ArithmeticException e) {
            IO.println("Помилка: ділення на нуль виконувати не можна.");
        } catch (InvalidInputException e) {
            IO.println("Помилка: " + e.getMessage());
        } finally {
            IO.println("Обробку запиту завершено.");
            scanner.close();
        }
    }
}
