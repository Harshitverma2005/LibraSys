package com.library.dao.interfaces;

import com.library.model.Transaction;
import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {
    int create(Transaction transaction) throws SQLException;
    Transaction findById(int id) throws SQLException;
    List<Transaction> findAll() throws SQLException;
    List<Transaction> findByUserId(int userId) throws SQLException;
    List<Transaction> findByBookId(int bookId) throws SQLException;
    List<Transaction> findOverdue() throws SQLException;
    boolean update(Transaction transaction) throws SQLException;
    boolean delete(int id) throws SQLException;
}
