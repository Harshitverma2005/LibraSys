package com.library.config;


public class AppConfig {
    // Application constants
    public static final String APP_NAME = "Library Management System";
    public static final String APP_VERSION = "1.0.0";

    // Business rules
    public static final int DEFAULT_BORROW_DAYS = 14;
    public static final double FINE_PER_DAY = 1.0;
    public static final int MAX_BOOKS_PER_USER = 5;

    // Display constants
    public static final int MAX_TITLE_DISPLAY_LENGTH = 30;
    public static final int MAX_AUTHOR_DISPLAY_LENGTH = 20;
    public static final int MAX_NAME_DISPLAY_LENGTH = 25;
    public static final int MAX_EMAIL_DISPLAY_LENGTH = 30;

    // Database constants
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int QUERY_TIMEOUT = 15;

    // Validation constants
    public static final int MIN_ISBN_LENGTH = 10;
    public static final int MAX_ISBN_LENGTH = 13;
    public static final int MAX_TITLE_LENGTH = 255;
    public static final int MAX_AUTHOR_LENGTH = 255;
    public static final int MAX_EMAIL_LENGTH = 255;
    public static final int MAX_PHONE_LENGTH = 15;

    private AppConfig() {
        // Prevent instantiation
    }
}
