package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db;";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Radha";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create tables
            createUsersTable(stmt);
            createBooksTable(stmt);
            createTransactionsTable(stmt);

            System.out.println("Database initialized successfully!");
        }
    }

    private static void createUsersTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                user_id SERIAL PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                email VARCHAR(255) UNIQUE NOT NULL,
                phone_number VARCHAR(15),
                user_type VARCHAR(20) DEFAULT 'MEMBER',
                status VARCHAR(20) DEFAULT 'ACTIVE',
                registration_date DATE DEFAULT CURRENT_DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        stmt.executeUpdate(sql);
    }

    private static void createBooksTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS books (
                book_id SERIAL PRIMARY KEY,
                isbn VARCHAR(13) UNIQUE NOT NULL,
                title VARCHAR(255) NOT NULL,
                author VARCHAR(255) NOT NULL,
                category VARCHAR(100),
                total_copies INTEGER DEFAULT 1,
                available_copies INTEGER DEFAULT 1,
                published_date DATE,
                status VARCHAR(20) DEFAULT 'AVAILABLE',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        stmt.executeUpdate(sql);
    }



    private static void createTransactionsTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id SERIAL PRIMARY KEY,
                book_id INTEGER REFERENCES books(book_id),
                user_id INTEGER REFERENCES users(user_id),
                borrow_date DATE NOT NULL,
                due_date DATE NOT NULL,
                return_date DATE,
                status VARCHAR(20) DEFAULT 'BORROWED',
                fine_amount DECIMAL(10,2) DEFAULT 0.00,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        stmt.executeUpdate(sql);
    }

}
