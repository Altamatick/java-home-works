package app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    // --- Book validation ---

    @Test
    void createBook_valid() {
        Book b = new Book("Clean Code", "Robert C. Martin");
        assertEquals("Clean Code", b.getTitle());
        assertEquals("Robert C. Martin", b.getAuthor());
        assertTrue(b.toString().contains("Clean Code"));
        assertTrue(b.toString().contains("Robert C. Martin"));
    }

    @Test
    void createBook_nullTitle_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null, "Author"));
    }

    @Test
    void createBook_blankAuthor_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Book("Title", "   "));
    }

    @Test
    void setters_validateAndTrim() {
        Book b = new Book("T", "A");
        b.setTitle("  New  ");
        b.setAuthor("  B  ");
        assertEquals("New", b.getTitle());
        assertEquals("B", b.getAuthor());
        assertThrows(IllegalArgumentException.class, () -> b.setTitle("   "));
        assertThrows(IllegalArgumentException.class, () -> b.setAuthor(null));
    }

    @Test
    void equals_hashCode_byTitleAuthor() {
        Book b1 = new Book("X", "Y");
        Book b2 = new Book("X", "Y");
        Book b3 = new Book("x", "Y");
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b1, b3); // регістр значущий
    }

    // --- Library core scenarios ---

    @Test
    void addAndCount() {
        Library lib = new Library();
        assertEquals(0, lib.getBookCount());
        lib.addBook(new Book("A", "B"));
        assertEquals(1, lib.getBookCount());
        lib.addBook(new Book("C", "D"));
        assertEquals(2, lib.getBookCount());
    }

    @Test
    void add_null_throws() {
        Library lib = new Library();
        assertThrows(IllegalArgumentException.class, () -> lib.addBook(null));
    }

    @Test
    void preventDuplicates() {
        Library lib = new Library();
        lib.addBook(new Book("A", "B"));
        lib.addBook(new Book("A", "B")); // дублікат ігнорується
        assertEquals(1, lib.getBookCount());
    }

    @Test
    void removeExisting_returnsTrue_andDecrements() {
        Library lib = new Library();
        Book b = new Book("A", "B");
        lib.addBook(b);
        assertTrue(lib.removeBook(b));
        assertEquals(0, lib.getBookCount());
    }

    @Test
    void removeNonExisting_returnsFalse() {
        Library lib = new Library();
        lib.addBook(new Book("A", "B"));
        assertFalse(lib.removeBook(new Book("C", "D")));
        assertEquals(1, lib.getBookCount());
    }

    @Test
    void remove_null_throws() {
        Library lib = new Library();
        assertThrows(IllegalArgumentException.class, () -> lib.removeBook(null));
    }

    @Test
    void getBooks_isUnmodifiableSnapshot() {
        Library lib = new Library();
        lib.addBook(new Book("A", "B"));
        List<Book> view = lib.getBooks();
        assertEquals(1, view.size());
        assertThrows(UnsupportedOperationException.class, () -> view.add(new Book("X", "Y")));
        // перевірка, що snapshot (не live-list): наступне додавання не змінює вже отриманий список
        lib.addBook(new Book("C", "D"));
        assertEquals(1, view.size());
        assertEquals(2, lib.getBookCount());
    }

    @Test
    void orderIsPreserved() {
        Library lib = new Library();
        Book b1 = new Book("1", "A");
        Book b2 = new Book("2", "B");
        Book b3 = new Book("3", "C");
        lib.addBook(b1);
        lib.addBook(b2);
        lib.addBook(b3);
        List<Book> list = lib.getBooks();
        assertEquals(List.of(b1, b2, b3), list);
    }
}
