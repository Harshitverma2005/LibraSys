package com.library.service;

import com.library.dao.impl.TransactionDAOImpl;
import com.library.dao.interfaces.TransactionDAO;
import com.library.exception.LibraryException;
import com.library.exception.TransactionException;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.User;
import com.library.model.enums.TransactionStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final BookService bookService;
    private final UserService userService;

    private static final int DEFAULT_BORROW_DAYS = 14;
    private static final double FINE_PER_DAY = 1.0;

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.bookService = new BookService();
        this.userService = new UserService();
    }

    public int borrowBook(int bookId, int userId) throws LibraryException {
        try {
            // Validate book exists and is available
            Book book = bookService.findBook(bookId);
            if (book.getAvailableCopies() <= 0) {
                throw new TransactionException("Book is not available for borrowing");
            }

            // Validate user exists
            User user = userService.findUser(userId);

            // Create transaction
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(DEFAULT_BORROW_DAYS);

            Transaction transaction = new Transaction(bookId, userId, borrowDate, dueDate);
            int transactionId = transactionDAO.create(transaction);

            // Update book availability
            bookService.updateAvailability(bookId, book.getAvailableCopies() - 1);

            return transactionId;
        } catch (SQLException e) {
            throw new LibraryException("Error borrowing book: " + e.getMessage(), e);
        }
    }

    public boolean returnBook(int transactionId) throws LibraryException {
        try {
            Transaction transaction = transactionDAO.findById(transactionId);
            if (transaction == null) {
                throw new TransactionException("Transaction with ID " + transactionId + " not found");
            }

            if (transaction.getStatus() != TransactionStatus.BORROWED) {
                throw new TransactionException("Book is not currently borrowed");
            }

            // Update transaction
            LocalDate returnDate = LocalDate.now();
            transaction.setReturnDate(returnDate);
            transaction.setStatus(TransactionStatus.RETURNED);

            // Calculate fine if overdue
            if (returnDate.isAfter(transaction.getDueDate())) {
                long daysOverdue = ChronoUnit.DAYS.between(transaction.getDueDate(), returnDate);
                double fineAmount = daysOverdue * FINE_PER_DAY;
                transaction.setFineAmount(fineAmount);
                transaction.setStatus(TransactionStatus.OVERDUE);
            }

            boolean updated = transactionDAO.update(transaction);

            // Update book availability
            if (updated) {
                Book book = bookService.findBook(transaction.getBookId());
                bookService.updateAvailability(transaction.getBookId(), book.getAvailableCopies() + 1);
            }

            return updated;
        } catch (SQLException e) {
            throw new LibraryException("Error returning book: " + e.getMessage(), e);
        }
    }

    public List<Transaction> getAllTransactions() throws LibraryException {
        try {
            return transactionDAO.findAll();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving transactions: " + e.getMessage(), e);
        }
    }

    public List<Transaction> getUserTransactions(int userId) throws LibraryException {
        try {
            return transactionDAO.findByUserId(userId);
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving user transactions: " + e.getMessage(), e);
        }
    }

    public List<Transaction> getBookTransactions(int bookId) throws LibraryException {
        try {
            return transactionDAO.findByBookId(bookId);
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving book transactions: " + e.getMessage(), e);
        }
    }

    public List<Transaction> getOverdueTransactions() throws LibraryException {
        try {
            return transactionDAO.findOverdue();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving overdue transactions: " + e.getMessage(), e);
        }
    }

    public Transaction findTransaction(int transactionId) throws LibraryException {
        try {
            Transaction transaction = transactionDAO.findById(transactionId);
            if (transaction == null) {
                throw new TransactionException("Transaction with ID " + transactionId + " not found");
            }
            return transaction;
        } catch (SQLException e) {
            throw new LibraryException("Error finding transaction: " + e.getMessage(), e);
        }
    }
}

