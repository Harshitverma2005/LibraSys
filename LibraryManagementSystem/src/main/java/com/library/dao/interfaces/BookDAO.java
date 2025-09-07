package com.library.dao.interfaces;

import com.library.model.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
    int create(Book book) throws SQLException;
    Book findById(int id) throws SQLException;
    Book findByIsbn(String isbn) throws SQLException;
    List<Book> findAll() throws SQLException;
    List<Book> searchBooks(String keyword) throws SQLException;
    boolean update(Book book) throws SQLException;
    boolean delete(int id) throws SQLException;
    boolean updateAvailability(int bookId, int availableCopies) throws SQLException;

}
