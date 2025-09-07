package com.library.ui;


import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.menuHandler = new MenuHandler(scanner);
        this.running = true;
    }

    public void start() {
        System.out.println("Welcome to Library Management System!");
        System.out.println("=====================================");

        while (running) {
            try {
                showMainMenu();
                int choice = getMenuChoice();
                handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }

        scanner.close();
        System.out.println("Thank you for using Library Management System!");
    }

    private void showMainMenu() {
        System.out.println("\n======= LIBRARY MANAGEMENT SYSTEM =======");
        System.out.println("1. Book Management");
        System.out.println("2. User Management");
        System.out.println("3. Transaction Management");
        System.out.println("4. Reports");
        System.out.println("5. System Status");
        System.out.println("0. Exit");
        System.out.println("==========================================");
    }

    private int getMenuChoice() {
        try {
            System.out.print("Enter your choice (0-5): ");
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                showBookMenu();
                break;
            case 2:
                showUserMenu();
                break;
            case 3:
                showTransactionMenu();
                break;
            case 4:
                showReportsMenu();
                break;
            case 5:
                showSystemStatus();
                break;
            case 0:
                System.out.println("Exiting system...");
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void showBookMenu() {
        System.out.println("\n=== BOOK MANAGEMENT ===");
        System.out.println("1. Add New Book");
        System.out.println("2. Search Books");
        System.out.println("3. Update Book");
        System.out.println("4. Delete Book");
        System.out.println("0. Back to Main Menu");

        int choice = getMenuChoice();
        switch (choice) {
            case 1:
                menuHandler.handleAddBook();
                break;
            case 2:
                menuHandler.handleSearchBooks();
                break;
            case 3:
                menuHandler.handleUpdateBook();
                break;
            case 4:
                menuHandler.handleDeleteBook();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void showUserMenu() {
        System.out.println("\n=== USER MANAGEMENT ===");
        System.out.println("1. Register New User");
        System.out.println("2. Search Users");
        System.out.println("3. Update User");
        System.out.println("0. Back to Main Menu");

        int choice = getMenuChoice();
        switch (choice) {
            case 1:
                menuHandler.handleRegisterUser();
                break;
            case 2:
                menuHandler.handleSearchUsers();
                break;
            case 3:
                menuHandler.handleUpdateUser();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void showTransactionMenu() {
        System.out.println("\n=== TRANSACTION MANAGEMENT ===");
        System.out.println("1. Borrow Book");
        System.out.println("2. Return Book");
        System.out.println("3. View Transactions");
        System.out.println("0. Back to Main Menu");

        int choice = getMenuChoice();
        switch (choice) {
            case 1:
                menuHandler.handleBorrowBook();
                break;
            case 2:
                menuHandler.handleReturnBook();
                break;
            case 3:
                menuHandler.handleViewTransactions();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void showReportsMenu() {
        System.out.println("\n=== REPORTS ===");
        System.out.println("1. Inventory Report");
        System.out.println("2. Overdue Books Report");
        System.out.println("0. Back to Main Menu");

        int choice = getMenuChoice();
        switch (choice) {
            case 1:
                menuHandler.handleInventoryReport();
                break;
            case 2:
                menuHandler.handleOverdueReport();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void showSystemStatus() {
        System.out.println("\n=== SYSTEM STATUS ===");
        try {
            // Test database connection
            com.library.util.DatabaseConnection.getConnection().close();
            System.out.println("✓ Database Connection: OK");
        } catch (Exception e) {
            System.out.println("✗ Database Connection: ERROR - " + e.getMessage());
        }

        System.out.println("✓ System Status: Running");
        System.out.println("✓ Java Version: " + System.getProperty("java.version"));
        System.out.println("✓ Application Version: 1.0.0");
    }
}
