    package com.library.model;

    import com.library.model.enums.BookStatus;
    import java.time.LocalDate;

    public class Book {
        private int bookId;
        private String isbn;
        private String title;
        private String author;
        private String category;
        private int totalCopies;
        private int availableCopies;
        private LocalDate publishedDate;
        private BookStatus status;
        private LocalDate createdAt;
        private LocalDate updatedAt;

        public Book() {}

        public Book(String isbn, String title, String author, String category,
                    int totalCopies, LocalDate publishedDate) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.category = category;
            this.totalCopies = totalCopies;
            this.availableCopies = totalCopies;
            this.publishedDate = publishedDate;
            this.status = BookStatus.AVAILABLE;
            this.createdAt = LocalDate.now();
            this.updatedAt = LocalDate.now();
        }

        // Getters and Setters
        public int getBookId() { return bookId; }
        public void setBookId(int bookId) { this.bookId = bookId; }

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public int getTotalCopies() { return totalCopies; }
        public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

        public int getAvailableCopies() { return availableCopies; }
        public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

        public LocalDate getPublishedDate() { return publishedDate; }
        public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

        public BookStatus getStatus() { return status; }
        public void setStatus(BookStatus status) { this.status = status; }

        public LocalDate getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

        public LocalDate getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

        @Override
        public String toString() {
            return String.format("Book{ID=%d, ISBN='%s', Title='%s', Author='%s', Available=%d/%d}",
                    bookId, isbn, title, author, availableCopies, totalCopies);
        }
    }


