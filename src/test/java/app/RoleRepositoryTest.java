package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @Test
    void testSaveRole() {
        Role role = new Role("USER");

        Role saved = roleRepository.save(role);

        assertNotNull(saved.getId());
        assertEquals("USER", saved.getName());
    }

    @Test
    void testFindByName() {
        Role role = new Role("USER");
        roleRepository.save(role);

        Optional<Role> found = roleRepository.findByName("USER");

        assertTrue(found.isPresent());
        assertEquals("USER", found.get().getName());
    }

    @Test
    void testFindByNameNotFound() {
        Optional<Role> found = roleRepository.findByName("NONEXISTENT");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        assertEquals(2, roleRepository.findAll().size());
    }
}
