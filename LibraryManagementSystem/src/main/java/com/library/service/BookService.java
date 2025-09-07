package com.library.service;

import com.library.dao.impl.BookDAOImpl;
import com.library.dao.interfaces.BookDAO;
import com.library.exception.BookNotFoundException;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;


public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAOImpl();
    }

    public int addBook(Book book) throws LibraryException {
        try {
            // Validate book data
            validateBook(book);

            // Check if ISBN already exists
            Book existingBook = bookDAO.findByIsbn(book.getIsbn());
            if (existingBook != null) {
                throw new LibraryException("Book with ISBN " + book.getIsbn() + " already exists");
            }

            return bookDAO.create(book);
        } catch (SQLException e) {
            throw new LibraryException("Error adding book: " + e.getMessage(), e);
        }
    }

    public Book findBook(int bookId) throws BookNotFoundException, LibraryException {
        try {
            Book book = bookDAO.findById(bookId);
            if (book == null) {
                throw new BookNotFoundException("Book with ID " + bookId + " not found");
            }
            return book;
        } catch (SQLException e) {
            throw new LibraryException("Error finding book: " + e.getMessage(), e);
        }
    }

    public List<Book> searchBooks(String keyword) throws LibraryException {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return bookDAO.findAll();
            }
            return bookDAO.searchBooks(keyword.trim());
        } catch (SQLException e) {
            throw new LibraryException("Error searching books: " + e.getMessage(), e);
        }
    }

    public List<Book> getAllBooks() throws LibraryException {
        try {
            return bookDAO.findAll();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving books: " + e.getMessage(), e);
        }
    }

    public boolean updateBook(Book book) throws LibraryException {
        try {
            validateBook(book);

            // Check if book exists
            Book existingBook = bookDAO.findById(book.getBookId());
            if (existingBook == null) {
                throw new BookNotFoundException("Book with ID " + book.getBookId() + " not found");
            }

            return bookDAO.update(book);
        } catch (SQLException e) {
            throw new LibraryException("Error updating book: " + e.getMessage(), e);
        }
    }

    public boolean deleteBook(int bookId) throws LibraryException {
        try {
            Book book = bookDAO.findById(bookId);
            if (book == null) {
                throw new BookNotFoundException("Book with ID " + bookId + " not found");
            }

            return bookDAO.delete(bookId);
        } catch (SQLException e) {
            throw new LibraryException("Error deleting book: " + e.getMessage(), e);
        }
    }

    public boolean updateAvailability(int bookId, int availableCopies) throws LibraryException {
        try {
            return bookDAO.updateAvailability(bookId, availableCopies);
        } catch (SQLException e) {
            throw new LibraryException("Error updating book availability: " + e.getMessage(), e);
        }
    }

    private void validateBook(Book book) throws LibraryException {
        if (!ValidationUtil.isNotEmpty(book.getTitle())) {
            throw new LibraryException("Book title is required");
        }
        if (!ValidationUtil.isNotEmpty(book.getAuthor())) {
            throw new LibraryException("Book author is required");
        }
        if (!ValidationUtil.isValidISBN(book.getIsbn())) {
            throw new LibraryException("Invalid ISBN format");
        }
        if (book.getTotalCopies() < 0) {
            throw new LibraryException("Total copies cannot be negative");
        }
        if (book.getAvailableCopies() < 0 || book.getAvailableCopies() > book.getTotalCopies()) {
            throw new LibraryException("Available copies must be between 0 and total copies");
        }
    }

}
