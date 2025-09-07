package com.library.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public static String formatForDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_FORMATTER) : "N/A";
    }

    public static String formatForDatabase(LocalDate date) {
        return date != null ? date.format(DEFAULT_FORMATTER) : null;
    }

    public static LocalDate parseFromString(String dateString) throws DateTimeParseException {
        return dateString != null && !dateString.trim().isEmpty()
                ? LocalDate.parse(dateString.trim(), DEFAULT_FORMATTER)
                : null;
    }

    public static boolean isValidDateString(String dateString) {
        try {
            parseFromString(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDate addDays(LocalDate date, int days) {
        return date != null ? date.plusDays(days) : null;
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        return start != null && end != null
                ? java.time.temporal.ChronoUnit.DAYS.between(start, end)
                : 0;
    }
}
