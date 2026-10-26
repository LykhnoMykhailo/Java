package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void successfulOperationDisplaysResultAndFinallyMessage() {
        String result = run("10\n2\n+\n");

        assertTrue(result.contains("Результат: 12.0"));
        assertTrue(result.contains("Обробку запиту завершено."));
    }

    @Test
    void divisionByZeroDisplaysErrorAndFinallyMessage() {
        String result = run("10\n0\n/\n");

        assertTrue(result.contains("Помилка: ділення на нуль виконувати не можна."));
        assertTrue(result.contains("Обробку запиту завершено."));
    }

    @Test
    void nonNumericInputDisplaysErrorAndFinallyMessage() {
        String result = run("abc\n2\n+\n");

        assertTrue(result.contains("Помилка: потрібно ввести число."));
        assertTrue(result.contains("Обробку запиту завершено."));
    }

    @Test
    void unknownOperationDisplaysInvalidInputErrorAndFinallyMessage() {
        String result = run("10\n2\n%\n");

        assertTrue(result.contains("Помилка: невідома операція"));
        assertTrue(result.contains("Обробку запиту завершено."));
    }

    private String run(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        Main.main(new String[0]);
        return output.toString(StandardCharsets.UTF_8);
    }
}
