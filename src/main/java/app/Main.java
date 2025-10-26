package app;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Library Management System Demo ===\n");

        // Create a library
        Library library = new Library();

        // Create some books
        Book book1 = new Book("Clean Code", "Robert C. Martin");
        Book book2 = new Book("Effective Java", "Joshua Bloch");
        Book book3 = new Book("Design Patterns", "Gang of Four");

        // Add books to library
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        System.out.println("Library has " + library.getBookCount() + " books");
        System.out.println("\nAll books in library:");
        List<Book> allBooks = library.getBooks();
        for (Book book : allBooks) {
            System.out.println("  - " + book);
        }

        // Find books by title (manual search)
        System.out.println("\nSearching for 'Clean Code':");
        for (Book book : library.getBooks()) {
            if (book.getTitle().equals("Clean Code")) {
                System.out.println("  Found: " + book);
            }
        }

        // Remove a book
        System.out.println("\nRemoving 'Effective Java'...");
        boolean removed = library.removeBook(book2);
        System.out.println("Removed: " + removed);
        System.out.println("Library now has " + library.getBookCount() + " books");

        System.out.println("\nDemo completed successfully!");
    }
}
