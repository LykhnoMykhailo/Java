package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void addReturnsSum() throws InvalidInputException {
        assertEquals(6.0, calculator.add(2.5, 3.5));
    }

    @Test
    void subtractReturnsDifference() throws InvalidInputException {
        assertEquals(5.5, calculator.subtract(10.0, 4.5));
    }

    @Test
    void multiplyReturnsProduct() throws InvalidInputException {
        assertEquals(-6.0, calculator.multiply(-2.0, 3.0));
    }

    @Test
    void divideReturnsQuotient() throws InvalidInputException {
        assertEquals(3.5, calculator.divide(7.0, 2.0));
    }

    @Test
    void divideByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10.0, 0.0));
    }

    @Test
    void divideByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10.0, -0.0));
    }

    @Test
    void negativeSquareRootThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> calculator.sqrt(-1.0));
    }

    @Test
    void nonFiniteInputThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> calculator.add(Double.NaN, 1.0));
        assertThrows(InvalidInputException.class, () -> calculator.multiply(Double.POSITIVE_INFINITY, 2.0));
    }
}
