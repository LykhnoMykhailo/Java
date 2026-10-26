package org.example;

public class Calculator {

    public double add(double a, double b) throws InvalidInputException {
        validateFinite(a, b);
        return a + b;
    }

    public double subtract(double a, double b) throws InvalidInputException {
        validateFinite(a, b);
        return a - b;
    }

    public double multiply(double a, double b) throws InvalidInputException {
        validateFinite(a, b);
        return a * b;
    }

    public double divide(double a, double b) throws InvalidInputException {
        validateFinite(a, b);
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

    public double sqrt(double value) throws InvalidInputException {
        validateFinite(value);
        if (value < 0) {
            throw new InvalidInputException("Cannot calculate the square root of a negative number");
        }
        return Math.sqrt(value);
    }

    private void validateFinite(double... values) throws InvalidInputException {
        for (double value : values) {
            if (!Double.isFinite(value)) {
                throw new InvalidInputException("Input values must be finite numbers");
            }
        }
    }
}
