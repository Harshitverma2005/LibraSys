package com.library;

import com.library.ui.ConsoleUI;
import com.library.util.DatabaseConnection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize database connection
            DatabaseConnection.initializeDatabase();
            System.out.println("=== Library Management System Started ===");

            // Start the console UI
            ConsoleUI consoleUI = new ConsoleUI();
            consoleUI.start();

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        }
    }
}
