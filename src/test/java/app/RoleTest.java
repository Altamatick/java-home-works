package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleCreation() {
        Role role = new Role("USER");

        assertNotNull(role);
        assertNull(role.getId());
        assertEquals("USER", role.getName());
        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }

    @Test
    void testRoleSetters() {
        Role role = new Role();

        role.setId(1L);
        role.setName("ADMIN");

        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testRoleEquals() {
        Role role1 = new Role("USER");
        role1.setId(1L);
        Role role2 = new Role("USER");
        role2.setId(1L);
        Role role3 = new Role("ADMIN");
        role3.setId(2L);

        assertEquals(role1, role2);
        assertNotEquals(role1, role3);
    }

    @Test
    void testRoleHashCode() {
        Role role1 = new Role("USER");
        role1.setId(1L);
        Role role2 = new Role("USER");
        role2.setId(1L);

        assertEquals(role1.hashCode(), role2.hashCode());
    }
}
