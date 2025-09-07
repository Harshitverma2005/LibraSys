package com.library.ui;

import com.library.util.ValidationUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {
    private final Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getValidString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    public int getValidInteger(String prompt) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid integer.");
            }
        }
    }

    public int getValidPositiveInteger(String prompt) {
        int value;
        do {
            value = getValidInteger(prompt);
            if (value <= 0) {
                System.out.println("Value must be positive. Please try again.");
            }
        } while (value <= 0);
        return value;
    }

    public String getValidEmail(String prompt) {
        String email;
        do {
            email = getValidString(prompt);
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("Invalid email format. Please try again.");
            }
        } while (!ValidationUtil.isValidEmail(email));
        return email;
    }

    public String getValidPhone(String prompt) {
        String phone;
        do {
            System.out.print(prompt + " (optional, press Enter to skip): ");
            phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                return null;
            }
            if (!ValidationUtil.isValidPhone(phone)) {
                System.out.println("Invalid phone number format. Please try again.");
            }
        } while (!ValidationUtil.isValidPhone(phone));
        return phone;
    }

    public String getValidISBN(String prompt) {
        String isbn;
        do {
            isbn = getValidString(prompt);
            if (!ValidationUtil.isValidISBN(isbn)) {
                System.out.println("Invalid ISBN format. Please enter a 10 or 13 digit ISBN.");
            }
        } while (!ValidationUtil.isValidISBN(isbn));
        return isbn;
    }

    public LocalDate getValidDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        while (true) {
            try {
                System.out.print(prompt + " (YYYY-MM-DD): ");
                String input = scanner.nextLine().trim();
                date = LocalDate.parse(input, formatter);
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
    }
}

