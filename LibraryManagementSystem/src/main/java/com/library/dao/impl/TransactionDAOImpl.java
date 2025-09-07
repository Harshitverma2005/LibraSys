package com.library.dao.impl;

import com.library.dao.interfaces.TransactionDAO;
import com.library.model.Transaction;
import com.library.model.enums.TransactionStatus;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public int create(Transaction transaction) throws SQLException {
        String sql = """
            INSERT INTO transactions (book_id, user_id, borrow_date, due_date, 
                                    status, fine_amount) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, transaction.getBookId());
            pstmt.setInt(2, transaction.getUserId());
            pstmt.setDate(3, Date.valueOf(transaction.getBorrowDate()));
            pstmt.setDate(4, Date.valueOf(transaction.getDueDate()));
            pstmt.setString(5, transaction.getStatus().name());
            pstmt.setDouble(6, transaction.getFineAmount());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public Transaction findById(int id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY borrow_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByUserId(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY borrow_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByBookId(int bookId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE book_id = ? ORDER BY borrow_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> findOverdue() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT * FROM transactions 
            WHERE status = 'BORROWED' AND due_date < CURRENT_DATE
            ORDER BY due_date ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        return transactions;
    }

    @Override
    public boolean update(Transaction transaction) throws SQLException {
        String sql = """
            UPDATE transactions SET return_date = ?, status = ?, fine_amount = ?, 
                                  updated_at = CURRENT_TIMESTAMP
            WHERE transaction_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (transaction.getReturnDate() != null) {
                pstmt.setDate(1, Date.valueOf(transaction.getReturnDate()));
            } else {
                pstmt.setNull(1, Types.DATE);
            }
            pstmt.setString(2, transaction.getStatus().name());
            pstmt.setDouble(3, transaction.getFineAmount());
            pstmt.setInt(4, transaction.getTransactionId());

            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setBookId(rs.getInt("book_id"));
        transaction.setUserId(rs.getInt("user_id"));
        transaction.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        transaction.setDueDate(rs.getDate("due_date").toLocalDate());

        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            transaction.setReturnDate(returnDate.toLocalDate());
        }

        transaction.setStatus(TransactionStatus.valueOf(rs.getString("status")));
        transaction.setFineAmount(rs.getDouble("fine_amount"));
        transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime().toLocalDate());
        transaction.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime().toLocalDate());
        return transaction;
    }

}
