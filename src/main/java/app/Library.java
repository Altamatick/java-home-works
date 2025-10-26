package app;

// Library.java
import java.util.*;

public class Library {
    private final Set<Book> books = new LinkedHashSet<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("book cannot be null");
        }
        books.add(book);
    }

    public boolean removeBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("book cannot be null");
        }
        return books.remove(book);
    }

    public List<Book> getBooks() {
        return List.copyOf(books);
    }

    public int getBookCount() {
        return books.size();
    }
}
