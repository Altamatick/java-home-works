package app;

import java.util.Objects;

public class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title  = normalize("title", title);
        this.author = normalize("author", author);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = normalize("title", title);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = normalize("author", author);
    }

    private static String normalize(String field, String value) {
        if (value == null) {
            throw new IllegalArgumentException(field + " cannot be null");
        }
        String v = value.trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return title.equals(book.title) && author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author='" + author + "'}";
    }
}
