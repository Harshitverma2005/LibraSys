package com.library.model;

import com.library.model.enums.TransactionStatus;
import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;
    private double fineAmount;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Constructors
    public Transaction() {}

    public Transaction(int bookId, int userId, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = TransactionStatus.BORROWED;
        this.fineAmount = 0.0;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    public boolean isOverdue() {
        return status == TransactionStatus.BORROWED && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("Transaction{ID=%d, BookID=%d, UserID=%d, Status=%s, Due=%s}",
                transactionId, bookId, userId, status, dueDate);
    }
}



