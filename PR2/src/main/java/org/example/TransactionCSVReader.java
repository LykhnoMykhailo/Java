package org.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    public static List<Transaction> readTransactions(String source) {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            try {
                return read(URI.create(source).toURL().openStream());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        try {
            return read(new FileInputStream(source));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Transaction> readResource(String resourceName) {
        InputStream is = TransactionCSVReader.class.getResourceAsStream(resourceName);
        if (is == null) {
            System.err.println("Resource not found: " + resourceName);
            return new ArrayList<>();
        }
        return read(is);
    }

    private static List<Transaction> read(InputStream is) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] values = line.split(",", -1);
                if (firstLine) {
                    firstLine = false;
                    try {
                        Double.parseDouble(values[1]);
                    } catch (NumberFormatException e) {
                        continue; // header row
                    }
                }
                transactions.add(new Transaction(values[0], Double.parseDouble(values[1]), values[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}