package com.library.ui;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.TransactionService;
import com.library.service.UserService;
import com.library.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuHandler {
    private final Scanner scanner;
    private final InputValidator validator;
    private final BookService bookService;
    private final UserService userService;
    private final TransactionService transactionService;

    public MenuHandler(Scanner scanner) {
        this.scanner = scanner;
        this.validator = new InputValidator(scanner);
        this.bookService = new BookService();
        this.userService = new UserService();
        this.transactionService = new TransactionService();
    }
    // ========== BOOK MANAGEMENT ==========
    public void handleAddBook() {
        try {
            System.out.println("\n=== Add New Book ===");

            String isbn = validator.getValidISBN("Enter ISBN: ");
            String title = validator.getValidString("Enter Title: ");
            String author = validator.getValidString("Enter Author: ");
            String category = validator.getValidString("Enter Category: ");
            int totalCopies = validator.getValidPositiveInteger("Enter Total Copies: ");
            LocalDate publishedDate = validator.getValidDate("Enter Published Date");

            Book book = new Book(isbn, title, author, category, totalCopies, publishedDate);

            int bookId = bookService.addBook(book);
            System.out.println("✓ Book added successfully with ID: " + bookId);

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleSearchBooks() {
        try {
            System.out.println("\n=== Search Books ===");
            System.out.print("Enter search keyword (title/author/category) or press Enter for all books: ");
            String keyword = scanner.nextLine().trim();

            List<Book> books = bookService.searchBooks(keyword);

            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }

            System.out.println("\n--- Search Results ---");
            System.out.printf("%-5s %-13s %-30s %-20s %-15s %-10s%n",
                    "ID", "ISBN", "Title", "Author", "Category", "Available");
            System.out.println("-".repeat(100));

            for (Book book : books) {
                System.out.printf("%-5d %-13s %-30s %-20s %-15s %d/%d%n",
                        book.getBookId(), book.getIsbn(),
                        truncate(book.getTitle(), 30),
                        truncate(book.getAuthor(), 20),
                        truncate(book.getCategory(), 15),
                        book.getAvailableCopies(), book.getTotalCopies());
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleUpdateBook() {
        try {
            System.out.println("\n=== Update Book ===");
            int bookId = validator.getValidInteger("Enter Book ID to update: ");

            Book book = bookService.findBook(bookId);
            System.out.println("Current book details: " + book);

            System.out.print("Enter new title (current: " + book.getTitle() + "): ");
            String title = scanner.nextLine().trim();
            if (!title.isEmpty()) book.setTitle(title);

            System.out.print("Enter new author (current: " + book.getAuthor() + "): ");
            String author = scanner.nextLine().trim();
            if (!author.isEmpty()) book.setAuthor(author);

            System.out.print("Enter new category (current: " + book.getCategory() + "): ");
            String category = scanner.nextLine().trim();
            if (!category.isEmpty()) book.setCategory(category);

            System.out.print("Enter new total copies (current: " + book.getTotalCopies() + "): ");
            String totalCopiesStr = scanner.nextLine().trim();
            if (!totalCopiesStr.isEmpty()) {
                int totalCopies = Integer.parseInt(totalCopiesStr);
                book.setTotalCopies(totalCopies);
                // Adjust available copies if needed
                if (book.getAvailableCopies() > totalCopies) {
                    book.setAvailableCopies(totalCopies);
                }
            }

            book.setUpdatedAt(LocalDate.now());

            if (bookService.updateBook(book)) {
                System.out.println("✓ Book updated successfully!");
            } else {
                System.out.println("✗ Failed to update book.");
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid number format.");
        }
    }

    public void handleDeleteBook() {
        try {
            System.out.println("\n=== Delete Book ===");
            int bookId = validator.getValidInteger("Enter Book ID to delete: ");

            Book book = bookService.findBook(bookId);
            System.out.println("Book to delete: " + book);

            System.out.print("Are you sure you want to delete this book? (y/N): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("yes")) {
                if (bookService.deleteBook(bookId)) {
                    System.out.println("✓ Book deleted successfully!");
                } else {
                    System.out.println("✗ Failed to delete book.");
                }
            } else {
                System.out.println("Book deletion cancelled.");
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ========== USER MANAGEMENT ==========
    public void handleRegisterUser() {
        try {
            System.out.println("\n=== Register New User ===");

            String firstName = validator.getValidString("Enter First Name: ");
            String lastName = validator.getValidString("Enter Last Name: ");
            String email = validator.getValidEmail("Enter Email: ");
            String phoneNumber = validator.getValidPhone("Enter Phone Number");

            User user = new User(firstName, lastName, email, phoneNumber);

            int userId = userService.registerUser(user);
            System.out.println("✓ User registered successfully with ID: " + userId);

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleSearchUsers() {
        try {
            System.out.println("\n=== Search Users ===");
            System.out.print("Enter search keyword (name/email) or press Enter for all users: ");
            String keyword = scanner.nextLine().trim();

            List<User> users = userService.searchUsers(keyword);

            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }

            System.out.println("\n--- Search Results ---");
            System.out.printf("%-5s %-25s %-30s %-15s %-10s%n",
                    "ID", "Name", "Email", "Phone", "Type");
            System.out.println("-".repeat(90));

            for (User user : users) {
                System.out.printf("%-5d %-25s %-30s %-15s %-10s%n",
                        user.getUserId(),
                        truncate(user.getFullName(), 25),
                        truncate(user.getEmail(), 30),
                        user.getPhoneNumber() != null ? truncate(user.getPhoneNumber(), 15) : "N/A",
                        user.getUserType());
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleUpdateUser() {
        try {
            System.out.println("\n=== Update User ===");
            int userId = validator.getValidInteger("Enter User ID to update: ");

            User user = userService.findUser(userId);
            System.out.println("Current user details: " + user);

            System.out.print("Enter new first name (current: " + user.getFirstName() + "): ");
            String firstName = scanner.nextLine().trim();
            if (!firstName.isEmpty()) user.setFirstName(firstName);

            System.out.print("Enter new last name (current: " + user.getLastName() + "): ");
            String lastName = scanner.nextLine().trim();
            if (!lastName.isEmpty()) user.setLastName(lastName);

            System.out.print("Enter new email (current: " + user.getEmail() + "): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                if (ValidationUtil.isValidEmail(email)) {
                    user.setEmail(email);
                } else {
                    System.out.println("Invalid email format, keeping current email.");
                }
            }

            System.out.print("Enter new phone number (current: " + user.getPhoneNumber() + "): ");
            String phoneNumber = scanner.nextLine().trim();
            if (!phoneNumber.isEmpty()) {
                if (ValidationUtil.isValidPhone(phoneNumber)) {
                    user.setPhoneNumber(phoneNumber);
                } else {
                    System.out.println("Invalid phone format, keeping current phone.");
                }
            }

            user.setUpdatedAt(LocalDate.now());

            if (userService.updateUser(user)) {
                System.out.println("✓ User updated successfully!");
            } else {
                System.out.println("✗ Failed to update user.");
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ========== TRANSACTION MANAGEMENT ==========
    public void handleBorrowBook() {
        try {
            System.out.println("\n=== Borrow Book ===");

            int bookId = validator.getValidInteger("Enter Book ID: ");
            int userId = validator.getValidInteger("Enter User ID: ");

            // Verify book and user exist
            Book book = bookService.findBook(bookId);
            User user = userService.findUser(userId);

            System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
            System.out.println("User: " + user.getFullName() + " (" + user.getEmail() + ")");
            System.out.println("Available copies: " + book.getAvailableCopies());

            if (book.getAvailableCopies() <= 0) {
                System.out.println("✗ Book is not available for borrowing.");
                return;
            }

            System.out.print("Confirm borrow operation? (y/N): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("yes")) {
                int transactionId = transactionService.borrowBook(bookId, userId);
                System.out.println("✓ Book borrowed successfully! Transaction ID: " + transactionId);
                System.out.println("Due date: " + LocalDate.now().plusDays(14));
            } else {
                System.out.println("Borrow operation cancelled.");
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleReturnBook() {
        try {
            System.out.println("\n=== Return Book ===");

            int transactionId = validator.getValidInteger("Enter Transaction ID: ");

            Transaction transaction = transactionService.findTransaction(transactionId);
            Book book = bookService.findBook(transaction.getBookId());
            User user = userService.findUser(transaction.getUserId());

            System.out.println("Transaction Details:");
            System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
            System.out.println("User: " + user.getFullName());
            System.out.println("Borrow Date: " + transaction.getBorrowDate());
            System.out.println("Due Date: " + transaction.getDueDate());
            System.out.println("Status: " + transaction.getStatus());

            if (transaction.isOverdue()) {
                System.out.println("⚠ WARNING: This book is overdue!");
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(transaction.getDueDate(), LocalDate.now());
                double estimatedFine = daysOverdue * 1.0;
                System.out.println("Estimated fine: $" + String.format("%.2f", estimatedFine));
            }

            System.out.print("Confirm return operation? (y/N): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("yes")) {
                if (transactionService.returnBook(transactionId)) {
                    System.out.println("✓ Book returned successfully!");

                    // Show final transaction details
                    Transaction updatedTransaction = transactionService.findTransaction(transactionId);
                    if (updatedTransaction.getFineAmount() > 0) {
                        System.out.println("Fine amount: $" + String.format("%.2f", updatedTransaction.getFineAmount()));
                    }
                } else {
                    System.out.println("✗ Failed to return book.");
                }
            } else {
                System.out.println("Return operation cancelled.");
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleViewTransactions() {
        try {
            System.out.println("\n=== View Transactions ===");
            System.out.println("1. All Transactions");
            System.out.println("2. User Transactions");
            System.out.println("3. Book Transactions");
            System.out.println("4. Overdue Transactions");

            int choice = validator.getValidInteger("Enter choice (1-4): ");
            List<Transaction> transactions = null;

            switch (choice) {
                case 1:
                    transactions = transactionService.getAllTransactions();
                    break;
                case 2:
                    int userId = validator.getValidInteger("Enter User ID: ");
                    transactions = transactionService.getUserTransactions(userId);
                    break;
                case 3:
                    int bookId = validator.getValidInteger("Enter Book ID: ");
                    transactions = transactionService.getBookTransactions(bookId);
                    break;
                case 4:
                    transactions = transactionService.getOverdueTransactions();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            if (transactions == null || transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("\n--- Transaction Results ---");
            System.out.printf("%-8s %-8s %-8s %-12s %-12s %-12s %-10s %-8s%n",
                    "Trans ID", "Book ID", "User ID", "Borrow Date", "Due Date", "Return Date", "Status", "Fine");
            System.out.println("-".repeat(90));

            for (Transaction transaction : transactions) {
                System.out.printf("%-8d %-8d %-8d %-12s %-12s %-12s %-10s $%-7.2f%n",
                        transaction.getTransactionId(),
                        transaction.getBookId(),
                        transaction.getUserId(),
                        transaction.getBorrowDate(),
                        transaction.getDueDate(),
                        transaction.getReturnDate() != null ? transaction.getReturnDate().toString() : "N/A",
                        transaction.getStatus(),
                        transaction.getFineAmount());
            }

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ========== REPORTS ==========
    public void handleInventoryReport() {
        try {
            System.out.println("\n=== Inventory Report ===");
            List<Book> books = bookService.getAllBooks();

            if (books.isEmpty()) {
                System.out.println("No books in inventory.");
                return;
            }

            int totalBooks = 0;
            int totalAvailable = 0;
            int totalBorrowed = 0;

            System.out.printf("%-5s %-30s %-20s %-8s %-8s %-8s%n",
                    "ID", "Title", "Author", "Total", "Available", "Borrowed");
            System.out.println("-".repeat(85));

            for (Book book : books) {
                int borrowed = book.getTotalCopies() - book.getAvailableCopies();

                System.out.printf("%-5d %-30s %-20s %-8d %-8d %-8d%n",
                        book.getBookId(),
                        truncate(book.getTitle(), 30),
                        truncate(book.getAuthor(), 20),
                        book.getTotalCopies(),
                        book.getAvailableCopies(),
                        borrowed);

                totalBooks += book.getTotalCopies();
                totalAvailable += book.getAvailableCopies();
                totalBorrowed += borrowed;
            }

            System.out.println("-".repeat(85));
            System.out.printf("%-56s %-8d %-8d %-8d%n", "TOTALS:", totalBooks, totalAvailable, totalBorrowed);

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    public void handleOverdueReport() {
        try {
            System.out.println("\n=== Overdue Books Report ===");
            List<Transaction> overdueTransactions = transactionService.getOverdueTransactions();

            if (overdueTransactions.isEmpty()) {
                System.out.println("✓ No overdue books found!");
                return;
            }

            System.out.printf("%-8s %-30s %-25s %-12s %-8s %-10s%n",
                    "Trans ID", "Book Title", "User Name", "Due Date", "Days Late", "Fine");
            System.out.println("-".repeat(100));

            double totalFines = 0;

            for (Transaction transaction : overdueTransactions) {
                try {
                    Book book = bookService.findBook(transaction.getBookId());
                    User user = userService.findUser(transaction.getUserId());

                    long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                            transaction.getDueDate(), LocalDate.now());
                    double fine = daysLate * 1.0;
                    totalFines += fine;

                    System.out.printf("%-8d %-30s %-25s %-12s %-8d $%-9.2f%n",
                            transaction.getTransactionId(),
                            truncate(book.getTitle(), 30),
                            truncate(user.getFullName(), 25),
                            transaction.getDueDate(),
                            daysLate,
                            fine);

                } catch (LibraryException e) {
                    System.out.println("Error loading details for transaction " + transaction.getTransactionId());
                }
            }

            System.out.println("-".repeat(100));
            System.out.printf("Total Outstanding Fines: $%.2f%n", totalFines);

        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ========== UTILITY METHODS ==========
    private String truncate(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }

}
