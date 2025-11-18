package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerCreationWithAllFields() {
        Customer customer = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");

        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Іван Петренко", customer.getFullName());
        assertEquals("ivan@example.com", customer.getEmail());
        assertEquals("1234567890", customer.getSocialSecurityNumber());
    }

    @Test
    void testCustomerCreationWithoutId() {
        Customer customer = new Customer("Марія Коваленко", "maria@example.com", "0987654321");

        assertNotNull(customer);
        assertNull(customer.getId());
        assertEquals("Марія Коваленко", customer.getFullName());
        assertEquals("maria@example.com", customer.getEmail());
        assertEquals("0987654321", customer.getSocialSecurityNumber());
    }

    @Test
    void testCustomerDefaultConstructor() {
        Customer customer = new Customer();

        assertNotNull(customer);
        assertNull(customer.getId());
        assertNull(customer.getFullName());
        assertNull(customer.getEmail());
        assertNull(customer.getSocialSecurityNumber());
    }

    @Test
    void testCustomerSetters() {
        Customer customer = new Customer();

        customer.setId(2L);
        customer.setFullName("Петро Сидоренко");
        customer.setEmail("petro@example.com");
        customer.setSocialSecurityNumber("1122334455");

        assertEquals(2L, customer.getId());
        assertEquals("Петро Сидоренко", customer.getFullName());
        assertEquals("petro@example.com", customer.getEmail());
        assertEquals("1122334455", customer.getSocialSecurityNumber());
    }

    @Test
    void testCustomerEquals() {
        Customer customer1 = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");
        Customer customer2 = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");
        Customer customer3 = new Customer(2L, "Іван Петренко", "ivan@example.com", "1234567890");

        assertEquals(customer1, customer2);
        assertNotEquals(customer1, customer3);
        assertEquals(customer1, customer1); // рефлексивність
    }

    @Test
    void testCustomerHashCode() {
        Customer customer1 = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");
        Customer customer2 = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");

        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    void testCustomerToString() {
        Customer customer = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");
        String toString = customer.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("fullName='Іван Петренко'"));
        assertTrue(toString.contains("email='ivan@example.com'"));
        assertTrue(toString.contains("socialSecurityNumber='1234567890'"));
    }

    @Test
    void testCustomerEqualsWithNull() {
        Customer customer = new Customer(1L, "Іван Петренко", "ivan@example.com", "1234567890");

        assertNotEquals(customer, null);
        assertNotEquals(customer, "not a customer");
    }
}
